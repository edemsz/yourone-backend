package bme.youronebackend.person

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "PERSON")
class Person() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1


    constructor(
        name: String,
        email: String, photo: String?, birthDate: LocalDate?,
    ) : this() {
        this.name = name
        this.email = email
        this.photo = photo
        this.birthDate = birthDate
    }


    @Column(nullable = false)
    lateinit var name: String


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


    @Column(nullable = true)
    var bio: String? = null

    @Column(nullable = true)
    var city: String? = null

    @Column(nullable = true)
    var jobType: Int? = null

    @Column(nullable = true)
    var eduLevel: Int? = null

    @Column(nullable = true)
    var cigarettes: Int? = null

    @Column(nullable = true)
    var alcohol: Int? = null

    @Column(nullable = true)
    var childrenNumber: Int? = null

    @Column(nullable = true)
    var maritalStatus: Int? = null

    @Column(nullable = true)
    var _musicalTaste: String? = null

    var musicalTaste: List<Int>
        @Transient get() = StringToIntListConverter.stringToIntList(_musicalTaste)
        set(value) {
            this._musicalTaste = StringToIntListConverter.intListToString(value)
        }


    @Column(nullable = true)
    var _filmTaste: String? = null

    var filmTaste: List<Int>
        @Transient get() = StringToIntListConverter.stringToIntList(_filmTaste)
        set(value) {
            this._filmTaste = StringToIntListConverter.intListToString(value)
        }

    @Column(nullable = true)
    var religion: Int? = null

    @Column(nullable = true)
    var horoscope: Int? = null

    @Column(nullable = true)
    var _languages: String? = null

    var languages: List<Int>
        @Transient get() = StringToIntListConverter.stringToIntList(_languages)
        set(value) {
            this._languages = StringToIntListConverter.intListToString(value)
        }


    @Column(nullable = true)
    var _interests: String? = null


    var interests: List<Int>
        @Transient get() = StringToIntListConverter.stringToIntList(_interests)
        set(value) {
            this._interests = StringToIntListConverter.intListToString(value)
        }


    @Column(nullable = true)
    var height: Int? = null


    @Column(nullable = true)
    var gender: Int? = null

    @Column(nullable = true)
    var tattoo: Int? = null

    @Column(nullable = true)
    var eyeColour: Int? = null

    @Column(nullable = true)
    var hairColour: Int? = null

    @Column(nullable = true)
    var piercing: Int? = null

    @Column(nullable = true)
    var glasses: Int? = null


    @Column(nullable = true)
    var sportiness: Int? = null

    @Column(nullable = true)
    var shape: Int? = null

    @Column(nullable = true)
    var breastSize: Int? = null

    @Column(nullable = true)
    var facialHair: Int? = null
}

object StringToIntListConverter {
    fun stringToIntList(s: String?): List<Int> =
        s?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() } ?: emptyList()

    fun intListToString(l: List<Int>): String = l.joinToString(",")
}