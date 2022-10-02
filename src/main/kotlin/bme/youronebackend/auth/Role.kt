package bme.youronebackend.auth

import javax.persistence.*

@Entity
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    lateinit var role:ERole
}