package bme.youronebackend.auth

import java.time.LocalDate


data class RegistrationDTO(
    var username: String,
    var password: String,
    var name: String,
    var email: String,
    var photo: String,
    var birthDate: LocalDate,
    var height: Int? = null,
    var age: Int? = null,
    var gender: String? = null,
    var tattoo: String? = null,
    var eyeColour: String? = null,
    var hairColour: String? = null,
    var piercing: String? = null,
    var glasses: String? = null,
    var beard: String? = null,
    var sportiness: String? = null,
    var breastSize: String? = null,
    var bio: String? = null,
    var city: String? = null,
    var cityType: String? = null,
    var jobType: String? = null,
    var eduLevel: String? = null,
    var cigarettes: String? = null,
    var alcohol: String? = null,
    var childrenNumber: String? = null,
    var maritalStatus: String? = null,
    var musicalTaste: String? = null,
    var filmTaste: String? = null,
    var religion: String? = null,
    var horoscope: String? = null,
    var languages: String? = null,
    var interests: String? = null,

)

data class LoginDTO(
    var username: String,
    var password: String,
)