package bme.youronebackend.person.yourone

import org.mapstruct.Mapper


@Mapper(componentModel = "spring")

abstract class YourOneMapper {

    abstract fun dtoToYourOne(dto: YourOneDTO): YourOneEntity
    abstract fun yourOneToDto(entity: YourOneEntity): YourOneDTO

}