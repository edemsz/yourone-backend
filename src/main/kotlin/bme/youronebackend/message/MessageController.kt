package bme.youronebackend.message

import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
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

        val message = messageService.sendMessage(chatMessageDto, sender)

        println("szólok socket")
        messagingTemplate.convertAndSendToUser(message.addressee.id.toString(),
            "/queue/messages",
            ChatNotification(message.id!!, message.sender.id, message.sender.name))
    }

    @GetMapping("api/chat/{addresseeId}/count")
    fun countNewMessages(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable addresseeId: Long,
    ): ResponseEntity<Int> {
        val sender = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(messageService.countNewMessages(sender, addresseeId))
    }

    @GetMapping("api/chat/{addresseeId}")
    fun findChatMessages(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable addresseeId: Long,
    ): ResponseEntity<List<MessageEntity>> {
        val sender = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(messageService.findChatMessages(sender, addresseeId))
    }

    @GetMapping("/api/chat")
    fun findAllChats(
        @RequestHeader("Authorization") authHeader: String?,
    ): ResponseEntity<List<Person>> {
        val user = personService.getCurrentMember(authHeader, null)
        return ResponseEntity.ok(messageService.findAllChatPartners(user))
    }

    @PostMapping("/api/chat/post-test")
    fun test(@RequestBody dto: SendMessageDTO): ResponseEntity<String> {
        println(dto.text)
        println(dto.addresseeId)
        val sender = personService.getById(502)//personService.getCurrentMember(authHeader, null)
        println("process message")

        val message = messageService.sendMessage(dto, sender)

        return ResponseEntity.ok("szia")
    }

    @GetMapping("/api/chat/all")
    @ApiOperation("Gets all chat messages, only for debug")
    fun findAllChats(
    ): ResponseEntity<List<MessageEntity>> = ResponseEntity.ok(messageService.allChats())


}
