package bme.youronebackend.message

import bme.youronebackend.person.Person
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
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api/chat")
class MessageController {
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    lateinit var personService: PersonService


    @MessageMapping("/chat")
    fun processMessage(@Payload chatMessageDto: SendMessageDTO) {
        println("kezdjünk bele tesó")
        //println(authHeader)
        println(chatMessageDto.text)
        println(chatMessageDto.addresseeId)
        val sender = personService.getById(502)//personService.getCurrentMember(authHeader, null)
        println("process message")

        val message=messageService.sendMessage(chatMessageDto, sender)

        println("szólok socket")
        messagingTemplate.convertAndSendToUser(message.addressee.id.toString(),
            "/queue/messages",
            ChatNotification(message.id!!, message.sender.id, message.sender.name))
    }

    @GetMapping("/{addresseeId}/count")
    fun countNewMessages(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable addresseeId: Long,
    ): ResponseEntity<Int> {
        val sender = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(messageService.countNewMessages(sender, addresseeId))
    }

    @GetMapping("/{addresseeId}")
    fun findChatMessages(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable addresseeId: Long,
    ): ResponseEntity<List<MessageEntity>> {
        val sender = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(messageService.findChatMessages(sender, addresseeId))
    }

    @GetMapping("/")
    fun findAllChats(
        @RequestHeader("Authorization") authHeader: String?,
        ) : ResponseEntity<List<Person>> {
        val user = personService.getCurrentMember(authHeader, null)
        return ResponseEntity.ok(messageService.findAllChatPartners(user))
    }


}
