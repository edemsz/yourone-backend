package bme.youronebackend.pair

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
    public fun checkPair(a: Person,b:Person):PairEntity?{
        val pair1=repository.findByAAndB(a,b)
        val pair2=repository.findByAAndB(b,a)
        if(pair1==null || pair2==null)
            return null
        return repository.save((PairEntity(a,b)))
    }
}