package bme.youronebackend.person

import bme.youronebackend.auth.JWTTools
import bme.youronebackend.auth.LoginDTO
import bme.youronebackend.auth.RegistrationDTO
import bme.youronebackend.auth.Tokens
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/person")
open class PersonController {
    @Autowired
    lateinit var personService: PersonService

    @Autowired
    lateinit var jwtTools: JWTTools

    @Autowired
    lateinit var personMapper: PersonMapper


    @GetMapping("/me")
    @ApiOperation("Gets the person entity of the current user")
    fun getMe(@RequestHeader("Authorization") authHeader: String?)
            : ResponseEntity<PersonAllDTO> {

        val person = personService.getCurrentMember(authHeader, null)
        val dto = personMapper.entityToDto(person)
        return ResponseEntity.ok(dto)
    }

    @PostMapping("/sign-up")
    @ApiOperation("Sign up method for users")
    fun register(@RequestBody registerData: RegistrationDTO): ResponseEntity<Tokens> {
        val person = personMapper.registrationDtoToEntity(registerData)
        personService.add(person)
        personService.register(registerData)

        val loginDTO = LoginDTO(registerData.username, registerData.password)
        return login(loginDTO)
    }

    @PostMapping("/login")
    @ApiOperation("Login method for users")
    fun login(@RequestBody loginData: LoginDTO): ResponseEntity<Tokens> {
        val tokens = personService.login(loginData)
        val jwtCookie = tokens.accessToken
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "jwt-cookie=$jwtCookie").body(tokens)
    }

    @PutMapping("/{id}")
    @ApiOperation("Modifying user data")
    fun changeUserData(@RequestBody newData: PersonAllDTO, @PathVariable(value = "id") id: Long)
            : ResponseEntity<PersonAllDTO> {
        val newPerson = personMapper.allDtoToEntity(newData)
        val changedPerson = personService.updateById(id, newPerson)
        val dto = personMapper.entityToDto(changedPerson)
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/next")
    @ApiOperation("Get next partner who can be swiped left or right")
    fun getNextPartner(@RequestHeader("Authorization") authHeader: String?)
            : ResponseEntity<PersonAllDTO> {
        val swipingPerson=personService.getCurrentMember(authHeader, null)
        val shownPerson = personService.getShownPerson(swipingPerson)
        val dto = personMapper.entityToDto(shownPerson)
        return ResponseEntity.ok(dto)
    }


}