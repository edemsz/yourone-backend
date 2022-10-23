package bme.youronebackend.person

import bme.youronebackend.auth.RegistrationDTO
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period

@Mapper(componentModel = "spring")
abstract class PersonMapper {
    abstract fun registrationDtoToEntity(dto: RegistrationDTO): Person
    abstract fun allDtoToEntity(dto: PersonAllDTO): Person

    abstract fun entityToDto(entity: Person): PersonAllDTO

    @AfterMapping
    fun fillReferences(person: Person, @MappingTarget dto: PersonAllDTO) {
        dto.age = Period.between(person.birthDate, LocalDate.now()).years
        dto.username = person.email
    }
}

@Component
class PersonMapperFacade {
    @Autowired
    private lateinit var personMapper: PersonMapper

    @Autowired
    private lateinit var personService: PersonService
    fun registrationDtoToEntity(dto: RegistrationDTO): Person = personMapper.registrationDtoToEntity(dto)
    fun allDtoToEntity(dto: PersonAllDTO): Person = personMapper.allDtoToEntity(dto)

    fun entityToDto(entity: Person): PersonAllDTO = personMapper.entityToDto(entity)
    fun pairToDto(mappingSource: Person, otherPerson: Person): PersonAllDTO {
        val dto = personMapper.entityToDto(mappingSource)
        dto.matchPct =personService.calculatePct(mappingSource,otherPerson)
        return dto
    }


}