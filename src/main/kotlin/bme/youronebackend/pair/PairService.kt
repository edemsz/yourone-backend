package bme.youronebackend.pair

import bme.youronebackend.basic.ResourceAlreadyExistsException
import bme.youronebackend.basic.ResourceNotFoundException
import bme.youronebackend.person.Person
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
open class PairService(
    var repository: PairRepository,
    ) {

    /**
     * Makes pair if not created yet, else returns with null
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    open fun checkPair(a: Person, b: Person): PairEntity? {
        if (a == b) return null
        val pair1 = repository.findByAAndB(a, b)
        val pair2 = repository.findByAAndB(b, a)
        if (pair1 != null || pair2 != null) return null
        return repository.saveAndFlush((PairEntity(a, b)))
    }

    /**
     * Gets the pair belonging to the two people
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    open fun getPair(a: Person, b: Person): PairEntity {
        val pair1 = repository.findByAAndB(a, b)
        if (pair1 != null) return pair1
        val pair2 = repository.findByAAndB(b, a)
        if (pair2 != null) return pair2
        throw ResourceNotFoundException()
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    open fun replyToPartner(replyingPerson: Person, otherPerson: Person, reply: Boolean): Boolean {
        val pair = getPair(replyingPerson, otherPerson)
        if (pair.a == replyingPerson) {
            if (pair.responseA != null) throw ResourceAlreadyExistsException()
            pair.responseA = reply
        }
        if (pair.b == replyingPerson) {
            if (pair.responseB != null) throw ResourceAlreadyExistsException()
            pair.responseB = reply
        }
        repository.save(pair)
        return pair.responseA == true && pair.responseB == true

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    open fun getMatchedPersonsByPerson(partner1: Person): List<Person> {
        val matchedPersons = mutableListOf<Person>()
        val matchesByA = repository.findAllByAAndState(partner1, PairState.MATCH)
        matchedPersons.addAll(matchesByA.map { it.b })
        val matchesByB = repository.findAllByBAndState(partner1, PairState.MATCH)
        matchedPersons.addAll(matchesByB.map { it.a })
        return matchedPersons
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    open fun getMatchesByPerson(partner1: Person): List<PairEntity> {
        val matches = mutableListOf<PairEntity>()
        val matchesByA = repository.findAllByAAndState(partner1, PairState.MATCH)
        matches.addAll(matchesByA)
        val matchesByB = repository.findAllByBAndState(partner1, PairState.MATCH)
        matches.addAll(matchesByB)
        return matches
    }


}