package bme.youronebackend.message


data class ChatNotification(
    val id: Long,
    val senderId: Long,
    val senderName: String,
    val text:String,
    val timeStamp:Long
)