package bme.youronebackend.pair

import bme.youronebackend.basic.ResourceNotFoundException
import bme.youronebackend.person.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PairService {
    @Autowired
    lateinit var repository: PairRepository

    /**
     * Makes pair if not created yet, else returns with null
     */
    fun checkPair(a: Person, b: Person): PairEntity? {
        if (a == b) return null
        val pair1 = repository.findByAAndB(a, b)
        val pair2 = repository.findByAAndB(b, a)
        if (pair1 == null || pair2 == null) return null
        return repository.save((PairEntity(a, b)))
    }

    /**
     * Gets the pair belonging to the two people
     */
    fun getPair(a: Person, b: Person): PairEntity {
        val pair1 = repository.findByAAndB(a, b)
        if (pair1 != null) return pair1
        val pair2 = repository.findByAAndB(b, a)
        if (pair2 != null) return pair2
        throw ResourceNotFoundException()
    }

    fun replyToPartner(replyingPerson: Person, otherPerson: Person, reply:Boolean) {
        val pair = getPair(replyingPerson, otherPerson)
        if (pair.a == replyingPerson) pair.responseA = reply
        if (pair.b == replyingPerson) pair.responseB = reply
        repository.save(pair)
    }

    fun getMatchedPersonsByPerson(partner1:Person):List<Person>{
        val matchedPersons= mutableListOf<Person>()
        val matchesByA=repository.findAllByAAndState(partner1,PairState.MATCH)
        matchedPersons.addAll(matchesByA.map { it.b })
        val matchesByB=repository.findAllByBAndState(partner1,PairState.MATCH)
        matchedPersons.addAll(matchesByB.map { it.a })
        return matchedPersons
    }




}