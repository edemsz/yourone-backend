package bme.youronebackend

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
    val text: String = ""

    @ManyToOne
    lateinit var sender: Person

    @ManyToOne
    lateinit var addressee: Person

    @Column
    val sentTime = Instant.now()
}