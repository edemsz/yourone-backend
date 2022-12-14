package bme.youronebackend.person

import bme.youronebackend.auth.*
import bme.youronebackend.basic.ResourceAlreadyExistsException
import bme.youronebackend.basic.ResourceNotFoundException
import bme.youronebackend.pair.PairService
import bme.youronebackend.person.yourone.YourOneEntity
import bme.youronebackend.person.yourone.YourOneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.Integer.max
import kotlin.math.roundToInt


@Service
open class PersonService
@Autowired constructor() {
    fun getAll(): MutableList<Person> {
        return repository.findAll()
    }

    @Autowired
    lateinit var repository: PersonRepository

    @Autowired
    lateinit var yourOneRepository: YourOneRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTools: JWTTools

    @Autowired
    lateinit var pairService: PairService


    fun add(e: Person): Person {
        e.theirOne?.let { yourOneRepository.save(it) }
        return repository.save(e)
    }

    fun updateById(id: Long, entity: Person): Person {
        if (id != entity.id && entity.id != -1L) {
            throw ResourceNotFoundException()
        }
        entity.password = repository.findById(id).get().password
        entity.id = id
        entity.theirOne?.let { yourOneRepository.save(it) }
        return repository.save(entity)

    }


    fun register(registerData: RegistrationDTO) {
        if (repository.countByEmail(registerData.username) > 1) throw ResourceAlreadyExistsException()
        val person = this.repository.findByEmail(registerData.username)!!
        person.password = passwordEncoder.encode(registerData.password)
        person.username = registerData.username
        this.updateById(person.id, person)
    }

    fun login(loginData: LoginDTO): Tokens {
        val authentication: Authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            loginData.username,
            loginData.password))


        SecurityContextHolder.getContext().authentication = authentication

        val user: User = authentication.principal as User
        val jwtCookie = jwtTools.createAccessToken(user.username, null, null)
        val tokens = Tokens(jwtCookie.toString(), "", getCurrentMember(jwtCookie, jwtCookie).id)
        return tokens
    }

    fun getCurrentMember(authHeader: String?, cookie: String?): Person {
        val jwt: String = authHeader ?: cookie ?: throw ResourceNotFoundException()
        val username = jwtTools.getUsernameFromJwt(jwt) ?: throw ResourceNotFoundException()
        val member = repository.findByEmail(username) ?: throw ResourceNotFoundException()
        return member
    }

    private fun mayBePairs(swipingPerson: Person, otherPerson: Person): Boolean {
        if ((swipingPerson.minAge == null) || (swipingPerson.maxAge == null) || (otherPerson.age in (swipingPerson.minAge!!..swipingPerson.maxAge!!))) if (swipingPerson.chemistry == null || calculatePct(
                swipingPerson.theirOne!!,
                otherPerson).pct > swipingPerson.chemistry!!
        ) if (isPotentialPair(swipingPerson, otherPerson))

            return true
        return false
    }

    fun getNextPersons(swipingPerson: Person): Array<Person> {

        val swipingGender = swipingPerson.gender
        val otherGender = if (swipingGender == 0) 1 else 0

        val potentialPairs = mutableListOf<Person>()
        val otherPeople = repository.findAllByGender(otherGender)
        val numberOfPeople = 10
        for (person in otherPeople) {
            if (mayBePairs(swipingPerson, person)) potentialPairs += person
            if (potentialPairs.size == numberOfPeople) return potentialPairs.toTypedArray()
        }
        return potentialPairs.toTypedArray()
    }

    private fun isPotentialPair(p1: Person, p2: Person): Boolean {
        val pair = pairService.checkPair(p1, p2)
        return pair != null
    }

    fun calculatePct(swipingUsersOne: YourOneEntity, otherPerson: Person): Match {
        val allAttributes = 18
        var matchingAttribute = 0.0
        val match = Match(0, emptyList())
        if (otherPerson.alcohol == swipingUsersOne.alcohol) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("alcohol", mapOf(Pair(otherPerson.alcohol!!, true)))
        } else match.commonAttributes += CommonAttributes("alcohol", mapOf(Pair(otherPerson.alcohol!!, false)))

        if (otherPerson.childrenNumber == swipingUsersOne.childrenNumber) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("childrenNumber",
                mapOf(Pair(otherPerson.childrenNumber!!, true)))
        } else match.commonAttributes += CommonAttributes("childrenNumber",
            mapOf(Pair(otherPerson.childrenNumber!!, false)))

        if (otherPerson.cigarettes == swipingUsersOne.cigarettes) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("cigarettes", mapOf(Pair(otherPerson.cigarettes!!, true)))
        } else match.commonAttributes += CommonAttributes("cigarettes", mapOf(Pair(otherPerson.cigarettes!!, false)))
        if (otherPerson.eduLevel == swipingUsersOne.eduLevel) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("eduLevel", mapOf(Pair(otherPerson.eduLevel!!, true)))
        } else match.commonAttributes += CommonAttributes("eduLevel", mapOf(Pair(otherPerson.eduLevel!!, false)))
        if (otherPerson.eyeColour == swipingUsersOne.eyeColour) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("eyeColour", mapOf(Pair(otherPerson.eyeColour!!, true)))
        } else match.commonAttributes += CommonAttributes("eyeColour", mapOf(Pair(otherPerson.eyeColour!!, false)))

        val filmTasteAttributes = CommonAttributes("filmTaste", emptyMap())
        otherPerson.filmTaste.forEach {
            if (swipingUsersOne.filmTaste.contains(it)) {
                matchingAttribute += 1.0 / swipingUsersOne.filmTaste.size
                filmTasteAttributes.matches += Pair(it, true)
            } else filmTasteAttributes.matches += Pair(it, false)
        }
        match.commonAttributes += filmTasteAttributes
        if (otherPerson.glasses == swipingUsersOne.glasses) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("glasses", mapOf(Pair(otherPerson.glasses!!, true)))
        } else match.commonAttributes += CommonAttributes("glasses", mapOf(Pair(otherPerson.glasses!!, false)))

        if (otherPerson.horoscope == swipingUsersOne.horoscope) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("horoscope", mapOf(Pair(otherPerson.horoscope!!, true)))
        } else match.commonAttributes += CommonAttributes("horoscope", mapOf(Pair(otherPerson.horoscope!!, false)))

        val interestAttributes = CommonAttributes("interests", emptyMap())
        otherPerson.interests.forEach {
            if (swipingUsersOne.interests.contains(it)) {
                matchingAttribute += 1.0 / swipingUsersOne.interests.size
                interestAttributes.matches += Pair(it, true)
            } else interestAttributes.matches += Pair(it, false)
        }
        match.commonAttributes += interestAttributes

        if (otherPerson.jobType == swipingUsersOne.jobType) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("jobType", mapOf(Pair(otherPerson.jobType!!, true)))
        } else match.commonAttributes += CommonAttributes("jobType", mapOf(Pair(otherPerson.jobType!!, false)))


        val languageAttributes = CommonAttributes("languages", emptyMap())
        otherPerson.languages.forEach {
            if (swipingUsersOne.languages.contains(it)) {
                matchingAttribute += 1.0 / swipingUsersOne.languages.size
                languageAttributes.matches += Pair(it, true)
            } else languageAttributes.matches += Pair(it, false)
        }
        match.commonAttributes += languageAttributes

        if (otherPerson.maritalStatus == swipingUsersOne.maritalStatus) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("maritalStatus", mapOf(Pair(otherPerson.maritalStatus!!, true)))
        } else match.commonAttributes += CommonAttributes("maritalStatus",
            mapOf(Pair(otherPerson.maritalStatus!!, false)))

        val musicalTasteAttributes = CommonAttributes("musicalTaste", emptyMap())
        otherPerson.musicalTaste.forEach {
            if (swipingUsersOne.musicalTaste.contains(it)) {
                matchingAttribute += 1.0 / swipingUsersOne.musicalTaste.size
                musicalTasteAttributes.matches += Pair(it, true)
            } else musicalTasteAttributes.matches += Pair(it, false)
        }
        match.commonAttributes += musicalTasteAttributes


        if (otherPerson.piercing == swipingUsersOne.piercing) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("piercing", mapOf(Pair(otherPerson.piercing!!, true)))
        } else match.commonAttributes += CommonAttributes("piercing", mapOf(Pair(otherPerson.piercing!!, false)))

        if (otherPerson.religion == swipingUsersOne.religion) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("religion", mapOf(Pair(otherPerson.religion!!, true)))
        } else match.commonAttributes += CommonAttributes("religion", mapOf(Pair(otherPerson.religion!!, false)))

        if (otherPerson.shape == swipingUsersOne.shape) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("shape", mapOf(Pair(otherPerson.shape!!, true)))
        } else match.commonAttributes += CommonAttributes("shape", mapOf(Pair(otherPerson.shape!!, false)))

        if (otherPerson.sportiness == swipingUsersOne.sportiness) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("sportiness", mapOf(Pair(otherPerson.sportiness!!, true)))
        } else match.commonAttributes += CommonAttributes("sportiness", mapOf(Pair(otherPerson.sportiness!!, false)))

        if (otherPerson.tattoo == swipingUsersOne.tattoo) {
            matchingAttribute++
            match.commonAttributes += CommonAttributes("tattoo", mapOf(Pair(otherPerson.tattoo!!, true)))
        } else match.commonAttributes += CommonAttributes("tattoo", mapOf(Pair(otherPerson.tattoo!!, false)))


        val matchPct = (matchingAttribute * 1.0 / allAttributes * 100).roundToInt()
        match.pct = matchPct
        return match
    }


    fun noMatch(denyingPerson: Person, otherPersonId: Long): Boolean {
        return noMatch(denyingPerson, repository.getById(otherPersonId))
    }

    private fun noMatch(denyingPerson: Person, otherPerson: Person): Boolean {
        return pairService.replyToPartner(denyingPerson, otherPerson, false)
    }

    fun yesMatch(denyingPerson: Person, otherPersonId: Long): Boolean {
        return yesMatch(denyingPerson, repository.getById(otherPersonId))
    }

    private fun yesMatch(denyingPerson: Person, otherPerson: Person): Boolean {
        return pairService.replyToPartner(denyingPerson, otherPerson, true)
    }

    fun makeMatch(aId: Long, bId: Long) {
        val a = getById(aId)
        val b = getById(bId)
        pairService.checkPair(a, b)
        pairService.replyToPartner(a, b, true)
        pairService.replyToPartner(b, a, true)
    }

    fun getMatches(swipingPerson: Person): List<Person> = pairService.getMatchedPersonsByPerson(swipingPerson)

    fun getById(id: Long): Person = repository.getById(id)
    private fun savePhoto(person: Person, filePath: String) {
        person.photos += Photo(filePath)
        updateById(person.id, person)
    }

    fun savePhoto(person: Person, filePath: String, index: Int?) {
        if (index == null) return savePhoto(person, filePath)
        person.photos.add(index, Photo(filePath))
        updateById(person.id, person)
    }

    fun changePictureOrder(swipingPerson: Person, oldNumber: Int, newNumber: Int): String {
        if (swipingPerson.photos.size < max(oldNumber, newNumber)) throw ResourceNotFoundException()
        val photo = swipingPerson.photos.removeAt(oldNumber)
        swipingPerson.photos.add(newNumber, photo)
        updateById(swipingPerson.id, swipingPerson)
        return photo.name
    }
}

@Service
class PersonAuthService : UserDetailsService {
    @Autowired
    lateinit var personRepository: PersonRepository

    override fun loadUserByUsername(email: String?): UserDetails {
        val member = personRepository.findByEmail(email!!)!!
        return buildUserFromMember(member)
    }

    private fun buildUserFromMember(member: Person): User {
        return User().fromPerson(member)
    }
}

data class Match(
    var pct: Int,
    var commonAttributes: List<CommonAttributes>,
)

data class CommonAttributes(
    val name: String,
    var matches: Map<Int, Boolean>,
)

