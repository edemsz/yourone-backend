package bme.youronebackend.message

import java.time.Instant
import java.time.LocalDateTime

data class ChatNotification(
    val id: Long,
    val senderId: Long,
    val senderName: String,
    val text:String,
    val timeStamp:LocalDateTime
)