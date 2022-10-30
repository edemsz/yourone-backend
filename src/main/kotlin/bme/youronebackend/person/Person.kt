package bme.youronebackend.person

import bme.youronebackend.person.yourone.YourOneEntity
import bme.youronebackend.person.yourone.YourOneEntityBase
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import java.time.Period
import javax.annotation.PostConstruct
import javax.persistence.*

@Entity
@Table(name = "PERSON")
class Person() : YourOneEntityBase() {

    constructor(
        name: String,
        email: String, birthDate: LocalDate?,
    ) : this() {
        this.name = name
        this.email = email
        this.birthDate = birthDate
    }

    @OneToOne(cascade = [CascadeType.ALL])
    var theirOne: YourOneEntity? = null

    @PostConstruct
    fun createTheirOne() {
        if (this.theirOne == null)
            this.theirOne = YourOneEntity()
    }

    @OneToMany(cascade = [CascadeType.ALL])
    lateinit var photos:MutableList<Photo>




    @Column(nullable = false)
    lateinit var name: String


    @Column(nullable = false)
    lateinit var email: String


    @Column(nullable = true)
    var birthDate: LocalDate? = null

    val age: Int
        @Transient get() = Period.between(this.birthDate, LocalDate.now()).years


    @Column(nullable = true)
    var username: String? = null

    @JsonIgnore
    @Column(nullable = true)
    var password: String? = null


    @Column(nullable = true)
    var bio: String? = null

    @Column(nullable = true)
    var city: String? = null


    @Column
    var minAge: Int? = null

    @Column
    var maxAge: Int? = null

    @Column
    var chemistry: Int? = null

}

@Entity
class Photo() {
    var name: String=""

    constructor(name: String) : this() {
        this.name = name
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

}

object StringToListConverter {
    fun stringToIntList(s: String?): List<Int> =
        s?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() } ?: emptyList()

    fun stringToStringList(s: String): List<String> {
        return s.split(",").filter { it.isNotEmpty() }
    }

    fun intListToString(l: List<Int>): String = l.joinToString(",")
    fun stringListToString(l: List<String>): String {
        return l.joinToString(",")
    }
}