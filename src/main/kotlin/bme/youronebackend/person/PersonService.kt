package bme.youronebackend.person

import bme.youronebackend.auth.*
import bme.youronebackend.basic.ResourceNotFoundException
import bme.youronebackend.pair.PairService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.math.roundToInt


@Service
class PersonService
@Autowired constructor() {
    fun getAll(): MutableList<Person> {
        return repository.findAll()
    }

    @Autowired
    lateinit var repository: PersonRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTools: JWTTools

    @Autowired
    lateinit var pairService: PairService


    fun add(e: Person): Person {
        return repository.save(e)
    }

    fun updateById(id: Long, entity: Person): Person {
        if (id != entity.id && entity.id != -1L) {
            throw ResourceNotFoundException()
        }
        entity.id = id
        return repository.save(entity)

    }


    fun register(registerData: RegistrationDTO) {
        val person = this.repository.findByUsername(registerData.username)!!
        person.password = passwordEncoder.encode(registerData.password)
        person.username = registerData.username
        this.updateById(person.id, person)
    }

    fun login(loginData: LoginDTO): Tokens {
        val authentication: Authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            loginData.username, loginData.password))


        SecurityContextHolder.getContext().authentication = authentication

        val user: User = authentication.principal as User
        val jwtCookie = jwtTools.createAccessToken(user.username, null, null)
        val tokens = Tokens(jwtCookie.toString(), "")
        return tokens
    }

    fun getCurrentMember(authHeader: String?, cookie: String?): Person {
        val jwt: String = authHeader ?: cookie ?: throw ResourceNotFoundException()
        val username = jwtTools.getUsernameFromJwt(jwt) ?: throw ResourceNotFoundException()
        val member = repository.findByUsername(username) ?: throw ResourceNotFoundException()
        return member
    }

    fun getNextPersons(swipingPerson: Person): Array<Person> {

        val swipingGender = swipingPerson.gender
        val otherGender = if (swipingGender == 0) 1 else 0

        val potentialPairs = mutableListOf<Person>()
        val otherPeople = repository.findAllByGender(otherGender)
        val numberOfPeople = 10
        for (person in otherPeople) {
            if (isPotentialPair(swipingPerson, person)) potentialPairs += person
            if (potentialPairs.size == numberOfPeople) return potentialPairs.toTypedArray()
        }
        return potentialPairs.toTypedArray()
    }

    private fun isPotentialPair(p1: Person, p2: Person): Boolean {
        val pair = pairService.checkPair(p1, p2)
        return pair != null
    }

    fun calculatePct(a: Person, b: Person): Int? {
        val allAttributes = 18
        var matchingAttribute = 0
        if (a.alcohol == b.alcohol) matchingAttribute++
        if (a.childrenNumber == b.childrenNumber) matchingAttribute++
        if (a.cigarettes == b.cigarettes) matchingAttribute++
        if (a.eduLevel == b.eduLevel) matchingAttribute++
        if (a.eyeColour == b.eyeColour) matchingAttribute++
        if (a.filmTaste == b.filmTaste) matchingAttribute++
        if (a.glasses == b.glasses) matchingAttribute++
        if (a.horoscope == b.horoscope) matchingAttribute++
        if (a.interests == b.interests) matchingAttribute++
        if (a.jobType == b.jobType) matchingAttribute++
        if (a.languages == b.languages) matchingAttribute++
        if (a.maritalStatus == b.maritalStatus) matchingAttribute++
        if (a.musicalTaste == b.musicalTaste) matchingAttribute++
        if (a.piercing == b.piercing) matchingAttribute++
        if (a.religion == b.religion) matchingAttribute++
        if (a.shape == b.shape) matchingAttribute++
        if (a.sportiness == b.sportiness) matchingAttribute++
        if (a.tattoo == b.tattoo) matchingAttribute++

        val matchPct = (matchingAttribute * 1.0 / allAttributes * 100).roundToInt()
        return matchPct
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

    fun getMatches(swipingPerson: Person): List<Person> =
        pairService.getMatchedPersonsByPerson(swipingPerson)

    fun getById(id: Long): Person = repository.getById(id)
}

@Service
class PersonAuthService : UserDetailsService {
    @Autowired
    lateinit var personRepository: PersonRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val member = personRepository.findByUsername(username!!) ?: throw UsernameNotFoundException("User not found")
        return buildUserFromMember(member)
    }

    private fun buildUserFromMember(member: Person): User {
        return User().fromPerson(member)
    }
}

data class Match(
    var pct: Double,
    var commonAttributes: List<CommonAttributes>,
)

data class CommonAttributes(
    val name: String,
    var matches: Map<Int, Boolean>,
)

