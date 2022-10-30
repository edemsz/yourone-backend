package bme.youronebackend.message

import bme.youronebackend.person.PersonMapperFacade
import bme.youronebackend.person.PersonService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import java.time.ZoneId
import java.time.ZonedDateTime


@Controller
class MessageController {
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    lateinit var personService: PersonService

    @Autowired
    private lateinit var personMapper: PersonMapperFacade


    @MessageMapping("/chat")
    fun processMessage(@Payload chatMessageDto: SendMessageDTO) {

        val sender = personService.getCurrentMember(chatMessageDto.jwt, null)

        val message = messageService.sendMessage(chatMessageDto, sender)

        val zdt: ZonedDateTime = message.sentTime.atZone(ZoneId.of("Europe/Budapest"))

        messagingTemplate.convertAndSendToUser(message.addressee.id.toString(),
            "/queue/messages",
            ChatNotification(message.id!!, message.sender.id, message.sender.name, message.text, zdt.toEpochSecond()))

        messagingTemplate.convertAndSendToUser(message.sender.id.toString(),
            "/queue/messages",
            ChatNotification(message.id, message.sender.id, message.sender.name, message.text, zdt.toEpochSecond()))

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
    ): ResponseEntity<List<RecentChatDTO>> {
        val user = personService.getCurrentMember(authHeader, null)
        val recentMessages = messageService.findAllChats(user)
        val recentChats = emptyList<RecentChatDTO>().toMutableList()
        for (m in recentMessages) {
            val partner = if (m.pair.a == user) m.pair.b else m.pair.a
            val partnerDto = personMapper.entityToDto(partner)
            val zdt: ZonedDateTime = m.sentTime.atZone(ZoneId.of("Europe/Budapest"))
            val chatNotification = ChatNotification(m.id!!, m.sender.id, m.sender.name, m.text, zdt.toEpochSecond())
            recentChats+=RecentChatDTO(partnerDto,chatNotification,messageService.countNewMessages(user,partner.id))
        }
        return ResponseEntity.ok(recentChats)
    }


    @GetMapping("/api/chat/all")
    @ApiOperation("Gets all chat messages, only for debug")
    fun findAllChats(
    ): ResponseEntity<List<MessageEntity>> = ResponseEntity.ok(messageService.allChats())


}
