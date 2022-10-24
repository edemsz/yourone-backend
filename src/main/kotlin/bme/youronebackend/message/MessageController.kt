package bme.youronebackend.message

import bme.youronebackend.person.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader


@Controller
class MessageController {
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    lateinit var personService: PersonService


    @MessageMapping("/chat")
    fun processMessage(@Payload chatMessageDto: SendMessageDTO, @RequestHeader("Authorization") authHeader: String?) {
        val sender = personService.getCurrentMember(authHeader, null)

        val message=messageService.sendMessage(chatMessageDto, sender)

        messagingTemplate.convertAndSendToUser(message.addressee.username!!,
            "/queue/messages",
            ChatNotification(message.id!!, message.sender.id, message.sender.name))
    }

    @GetMapping("/messages/{addresseeId}/count")
    fun countNewMessages(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable addresseeId: Long,
    ): ResponseEntity<Int> {
        val sender = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(messageService.countNewMessages(sender, addresseeId))
    }

    @GetMapping("/messages/{addresseeId}")
    fun findChatMessages(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable addresseeId: Long,
    ): ResponseEntity<List<MessageEntity>> {
        val sender = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(messageService.findChatMessages(sender, addresseeId))
    }


}
