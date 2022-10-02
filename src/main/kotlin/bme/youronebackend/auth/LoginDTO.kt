package bme.youronebackend.auth

import java.time.LocalDate


data class RegistrationDTO (
    var username:String,
    var password:String,
    var firstName:String,
    var lastName:String,
    var email:String,
    var photo:String,
    var birthDate:LocalDate
)

data class LoginDTO(
    var username:String,
    var password:String
)