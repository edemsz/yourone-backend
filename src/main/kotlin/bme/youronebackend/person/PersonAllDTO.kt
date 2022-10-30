package bme.youronebackend.person

import bme.youronebackend.person.yourone.YourOneDTO
import java.time.LocalDate

data class PersonAllDTO(

    var id: Long,
    var username: String?,
    var name: String,
    var email: String,
    var photos: List<String>?,
    var birthDate: LocalDate,
    var height: Int? = null,
    var age: Int? = null,
    var gender: Int? = null,
    var tattoo: Int? = null,
    var eyeColour: Int? = null,
    var hairColour: Int? = null,
    var piercing: Int? = null,
    var glasses: Int? = null,
    var sportiness: Int? = null,
    var breastSize: Int? = null,
    var bio: String? = null,
    var city: String? = null,
    var jobType: Int? = null,
    var eduLevel: Int? = null,
    var cigarettes: Int? = null,
    var alcohol: Int? = null,
    var childrenNumber: Int? = null,
    var maritalStatus: Int? = null,
    var musicalTaste: List<Int>? = null,
    var filmTaste: List<Int>? = null,
    var religion: Int? = null,
    var horoscope: Int? = null,
    var languages: List<Int>? = null,
    var interests: List<Int>? = null,
    var shape: Int? = null,
    var facialHair: Int? = null,
    var match: Match? = null,

    var minAge: Int? = null,
    var maxAge: Int? = null,
    var chemistry: Int? = null,

    var theirOne: YourOneDTO? = null,

    )

data class CreatePersonDTO(

    var username: String?,
    var name: String,
    var email: String,
    var birthDate: LocalDate,
    var height: Int? = null,
    var age: Int? = null,
    var gender: Int? = null,
    var tattoo: Int? = null,
    var eyeColour: Int? = null,
    var hairColour: Int? = null,
    var piercing: Int? = null,
    var glasses: Int? = null,
    var sportiness: Int? = null,
    var breastSize: Int? = null,
    var bio: String? = null,
    var city: String? = null,
    var jobType: Int? = null,
    var eduLevel: Int? = null,
    var cigarettes: Int? = null,
    var alcohol: Int? = null,
    var childrenNumber: Int? = null,
    var maritalStatus: Int? = null,
    var musicalTaste: List<Int>? = null,
    var filmTaste: List<Int>? = null,
    var religion: Int? = null,
    var horoscope: Int? = null,
    var languages: List<Int>? = null,
    var interests: List<Int>? = null,
    var shape: Int? = null,
    var facialHair: Int? = null,
    var match: Match? = null,

    var minAge: Int? = null,
    var maxAge: Int? = null,
    var chemistry: Int? = null,

    var theirOne: YourOneDTO? = null,

    )