package bme.youronebackend.person

import bme.youronebackend.auth.*
import bme.youronebackend.basic.ResourceAlreadyExistsException
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
        e.setUid()
        return repository.save(e)
    }

    fun updateById(id: Long, entity: Person): Person {
        entity.uid=repository.getById(id).uid
        if (id != entity.id && entity.id!=-1L) {
            throw ResourceNotFoundException()
        }
        entity.id = id
        return repository.save(entity)

    }




    fun setPassword(uid:String,passwordEncoded:String){
        val person=repository.findByUid(uid)
        person?.password=passwordEncoded
        repository.save(person!!)

    }

    fun register(registerData: RegistrationDTO) {
        if(repository.existsByUsername(registerData.username)){
            throw ResourceAlreadyExistsException()
        }

        val personFromDTO=Person(registerData.firstName,registerData.lastName
            ,registerData.email,registerData.photo,registerData.birthDate)

        val person=this.add(personFromDTO)
        person!!.password=passwordEncoder.encode(registerData.password)
        person.username=registerData.username
        this.updateById(person.id,person)
    }

    fun login(loginData: LoginDTO): Tokens {
        val authentication: Authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(loginData.username, loginData.password))


        SecurityContextHolder.getContext().authentication = authentication

        val user: User = authentication.principal as User
        val jwtCookie = jwtTools.createAccessToken(user.username,null,null)
        val tokens= Tokens(jwtCookie.toString(),"")
        return tokens
    }

    fun getCurrentMember(authHeader:String?,cookie:String?):Person{
        val jwt:String=authHeader?:cookie?:throw ResourceNotFoundException()
        val username= jwtTools.getUsernameFromJwt(jwt) ?: throw ResourceNotFoundException()
        val member=repository.findByUsername(username) ?: throw ResourceNotFoundException()
        return member
    }
}

@Service
class PersonAuthService:UserDetailsService{
    @Autowired
    lateinit var personRepository: PersonRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val member=personRepository.findByUsername(username!!)?: throw UsernameNotFoundException("User not found")
        return buildUserFromMember(member)
    }
    private fun buildUserFromMember(member: Person): User {
        return User().fromPerson(member)
    }
}