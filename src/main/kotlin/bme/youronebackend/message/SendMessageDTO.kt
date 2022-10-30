package bme.youronebackend.message

import bme.youronebackend.person.PersonAllDTO

class SendMessageDTO {
    var text: String = ""
    var addresseeId: Long = -1
    var jwt: String = ""
}

data class ChatNotification(
    val id: Long,
    val senderId: Long,
    val senderName: String,
    val text: String,
    val timeStamp: Long,
)

data class RecentChatDTO(
    val partner: PersonAllDTO?=null,
    var lastMessage: ChatNotification?=null,
    val newMessagesCount: Int=0,
)