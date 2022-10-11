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
        this.name=name
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
    var cityType: String? = null

    @Column(nullable = true)
    var jobType: String? = null

    @Column(nullable = true)
    var eduLevel: String? = null

    @Column(nullable = true)
    var cigarettes: String? = null

    @Column(nullable = true)
    var alcohol: String? = null

    @Column(nullable = true)
    var childrenNumber: String? = null

    @Column(nullable = true)
    var maritalStatus: String? = null

    @Column(nullable = true)
    var musicalTaste: String? = null

    @Column(nullable = true)
    var filmTaste: String? = null

    @Column(nullable = true)
    var religion: String? = null

    @Column(nullable = true)
    var horoscope: String? = null

    @Column(nullable = true)
    var languages: String? = null

    @Column(nullable = true)
    var interests: String? = null


    @Column(nullable = true)
    var height: Int? = null


    @Column(nullable = true)
    var gender: String? = null

    @Column(nullable = true)
    var tattoo: String? = null

    @Column(nullable = true)
    var eyeColour: String? = null

    @Column(nullable = true)
    var hairColour: String? = null

    @Column(nullable = true)
    var piercing: String? = null

    @Column(nullable = true)
    var glasses: String? = null

    @Column(nullable = true)
    var beard: String? = null

    @Column(nullable = true)
    var sportiness: String? = null

    @Column(nullable = true)
    var breastSize: String? = null


}