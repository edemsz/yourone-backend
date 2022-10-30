package bme.youronebackend.person.yourone

import org.springframework.data.jpa.repository.JpaRepository

interface YourOneRepository : JpaRepository<YourOneEntity, Long> {

}