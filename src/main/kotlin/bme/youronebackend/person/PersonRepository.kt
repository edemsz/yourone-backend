package bme.youronebackend.person

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long> {
    fun findByUsername(username:String):Person?
    fun findByUid(uid:String):Person?
    fun existsByUsername(username:String):Boolean
    fun existsByUid(uid:String):Boolean

}