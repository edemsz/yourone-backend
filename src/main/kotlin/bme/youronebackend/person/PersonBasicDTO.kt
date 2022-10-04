package bme.youronebackend.person

import java.time.LocalDate

data class PersonAllDTO (
    var username:String,
    var firstName:String,
    var lastName:String,
    var email:String,
    var photo:String,
    var birthDate: LocalDate
)