package bme.youronebackend.person

import bme.youronebackend.auth.RegistrationDTO
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import java.time.LocalDate
import java.time.Period

@Mapper(componentModel = "spring")
abstract class PersonMapper {
    abstract fun registrationDtoToEntity(dto: RegistrationDTO): Person
    abstract fun allDtoToEntity(dto: PersonAllDTO): Person

    abstract fun entityToDto(entity: Person): PersonAllDTO
    @AfterMapping
    fun fillReferences(person: Person, @MappingTarget dto: PersonAllDTO){
        dto.age=Period.between(person.birthDate, LocalDate.now()).years
        dto.username=person.email

    }


}