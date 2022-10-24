package bme.youronebackend.message

import bme.youronebackend.pair.PairService
import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class MessageService {
    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var pairService: PairService

    @Autowired
    private lateinit var personService: PersonService

    fun sendMessage(messageDto: SendMessageDTO, sender: Person): MessageEntity {
        val message = MessageEntity()
        message.text = messageDto.text
        message.addressee = personService.getById(messageDto.addresseeId)
        message.sender = sender
        message.pair = pairService.getPair(sender, message.addressee)
        return messageRepository.save(message)

    }


    fun countNewMessages(sender: Person, addresseeId: Long): Int {
        val addressee = personService.getById(addresseeId)
        return messageRepository.countByAddresseeAndSenderAndState(sender, addressee, MessageState.SENT)
    }

    fun findChatMessages(sender: Person, addresseeId: Long): List<MessageEntity> {
        val addressee = personService.getById(addresseeId)
        val pair = pairService.getPair(sender, addressee)
        val messages = messageRepository.findAllByPair(pair)
        deliverMessages(messages)
        return messages
    }

    private fun deliverMessages(messages: List<MessageEntity>) {
        val notDeliveredMessages = messages.filter { it.state == MessageState.SENT }
        notDeliveredMessages.forEach {
            it.state = MessageState.DELIVERED
            messageRepository.save(it)
        }
    }


}