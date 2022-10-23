package bme.youronebackend.pair

import bme.youronebackend.person.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PairRepository:JpaRepository<PairEntity,Long> {
    fun findByAAndB(a:Person,b:Person):PairEntity?
    fun findAllByAAndState(a:Person,s:PairState):List<PairEntity>
    fun findAllByBAndState(b:Person,s:PairState):List<PairEntity>
}