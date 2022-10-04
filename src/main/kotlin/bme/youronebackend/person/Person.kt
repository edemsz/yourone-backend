package bme.youronebackend.person

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "PERSON")
class Person()  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1


    constructor(
        firstName: String, lastName: String,
        email: String, photo: String?, birthDate: LocalDate?,
    ) : this() {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.photo = photo
        this.birthDate = birthDate
        this.setUid()
    }

    fun setUid(): Person {
        val stringLength = 10
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..stringLength)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        this.uid = randomString
        return this
    }


    @Column(nullable = false)
    lateinit var firstName: String

    @Column(nullable = false)
    lateinit var lastName: String

    @Column(nullable = false)
    lateinit var email: String

    @Column(nullable = true)
    var photo: String? = null

    @Column(nullable = true)
    var birthDate: LocalDate? = null


    @Column(nullable = true)
    var username: String? = null

    @JsonIgnore
    @Column(nullable = true)
    var password: String? = null

    @Column(nullable = false, name = "user_id")
    lateinit var uid: String

    @Column(nullable = true)
    var bio:String?=null




}