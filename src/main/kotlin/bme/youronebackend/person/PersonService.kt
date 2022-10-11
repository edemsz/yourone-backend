package bme.youronebackend.person

import bme.youronebackend.auth.*
import bme.youronebackend.basic.ResourceNotFoundException
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
import kotlin.random.Random


@Service
class PersonService
@Autowired
constructor() {
    @Autowired
    lateinit var repository: PersonRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTools: JWTTools


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
        val authentication: Authentication = authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(loginData.username, loginData.password))


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

    fun getShownPerson(swipingPerson: Person): Person {
        val randint = Random.nextLong(400)
        return repository.getById(randint)
    }
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