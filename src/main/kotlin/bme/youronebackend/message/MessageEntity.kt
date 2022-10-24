package bme.youronebackend.message

import bme.youronebackend.pair.PairEntity
import bme.youronebackend.person.Person
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class MessageEntity {
    @Id
    @Column(name = "id", nullable = false)
    val id: Long? = null

    @Column
    var text: String = ""

    @ManyToOne
    lateinit var pair:PairEntity

    @ManyToOne
    lateinit var sender: Person

    @ManyToOne
    lateinit var addressee: Person

    @Column
    val sentTime = Instant.now()

    @Column
    var state: MessageState? = MessageState.SENT
}


enum class MessageState{
    SENT,DELIVERED,SEEN
}