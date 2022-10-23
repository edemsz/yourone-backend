package bme.youronebackend.pair

import bme.youronebackend.person.Person
import javax.persistence.*


@Entity
class PairEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1


    @ManyToOne
    lateinit var a: Person

    @ManyToOne
    lateinit var b: Person

    constructor(a: Person, b: Person) : this() {
        this.a = a
        this.b = b
    }

    @Column
    var responseA: Boolean? = null

    @Column
    var responseB: Boolean? = null

    @Column
    var state: PairState = PairState.NOT_DECIDED

    @PreUpdate
    fun setState() {
        if (responseA == false || responseB == false) state = PairState.DENIED
        if (responseA == true && responseB == true) state = PairState.MATCH
    }

}

enum class PairState {
    NOT_DECIDED, DENIED, MATCH
}