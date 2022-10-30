package bme.youronebackend.person

import bme.youronebackend.auth.RegistrationDTO
import bme.youronebackend.person.yourone.YourOneMapper
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period

@Mapper(componentModel = "spring", uses = [YourOneMapper::class])

abstract class PersonMapper {

    abstract fun registrationDtoToEntity(dto: RegistrationDTO): Person

    abstract fun createDtoToEntity(dto: CreatePersonDTO): Person

    @Mapping(target = "photos", ignore = true)
    abstract fun entityToDto(entity: Person): PersonAllDTO

    @AfterMapping
    fun fillReferences(person: Person, @MappingTarget dto: PersonAllDTO) {
        dto.photos = person.photos.map { it.name }
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
    fun allDtoToEntity(dto: CreatePersonDTO): Person = personMapper.createDtoToEntity(dto)

    fun entityToDto(entity: Person): PersonAllDTO = personMapper.entityToDto(entity)
    fun pairToDto(mappingSource: Person, otherPerson: Person): PersonAllDTO {
        val dto = personMapper.entityToDto(mappingSource)
        dto.match = personService.calculatePct(mappingSource, otherPerson)
        return dto
    }


}