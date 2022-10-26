package bme.youronebackend.auth

data class Tokens (
    var accessToken:String,
    var refreshToken:String,
    var userId:Long
    )