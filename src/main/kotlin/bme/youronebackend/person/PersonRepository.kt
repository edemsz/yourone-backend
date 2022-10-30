package bme.youronebackend.person

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long> {
    fun findByEmail(email: String): Person?
    fun countByEmail(email: String): Int
    fun existsByUsername(username: String): Boolean
    fun findAllByGender(gender: Int): List<Person>
}