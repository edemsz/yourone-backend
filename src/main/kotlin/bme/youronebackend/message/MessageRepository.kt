package bme.youronebackend.message

import bme.youronebackend.pair.PairEntity
import bme.youronebackend.person.Person
import org.springframework.data.jpa.repository.JpaRepository


interface MessageRepository : JpaRepository<MessageEntity, Long> {
    fun findFirstByPairOrderBySentTimeDesc(p: PairEntity): MessageEntity
    fun findAllByPair(p: PairEntity): List<MessageEntity>
    fun countByAddresseeAndSenderAndState(addressee: Person, sender: Person, state: MessageState): Int
    fun existsByPair(p: PairEntity): Boolean
}