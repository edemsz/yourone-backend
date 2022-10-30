package bme.youronebackend.person.yourone

import bme.youronebackend.person.StringToListConverter
import javax.persistence.*


@MappedSuperclass
open class YourOneEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1


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
        @Transient get() = StringToListConverter.stringToIntList(_musicalTaste)
        set(value) {
            this._musicalTaste = StringToListConverter.intListToString(value)
        }


    @Column(nullable = true)
    var _filmTaste: String? = null

    var filmTaste: List<Int>
        @Transient get() = StringToListConverter.stringToIntList(_filmTaste)
        set(value) {
            this._filmTaste = StringToListConverter.intListToString(value)
        }

    @Column(nullable = true)
    var religion: Int? = null

    @Column(nullable = true)
    var horoscope: Int? = null

    @Column(nullable = true)
    var _languages: String? = null

    var languages: List<Int>
        @Transient get() = StringToListConverter.stringToIntList(_languages)
        set(value) {
            this._languages = StringToListConverter.intListToString(value)
        }


    @Column(nullable = true)
    var _interests: String? = null


    var interests: List<Int>
        @Transient get() = StringToListConverter.stringToIntList(_interests)
        set(value) {
            this._interests = StringToListConverter.intListToString(value)
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

@Entity
class YourOneEntity: YourOneEntityBase()