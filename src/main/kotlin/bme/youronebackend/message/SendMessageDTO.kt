package bme.youronebackend.message

data class SendMessageDTO(
    var text: String = "",
    var addresseeId: Long,
    )
