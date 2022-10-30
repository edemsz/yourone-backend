package bme.youronebackend.person

data class ItsAMatch(
    val partnerId:Long,
    val partnerName:String,
    val partnerPhoto:String?,
    val myId:Long,
    val myName:String,
    val myPhoto:String?,


)
