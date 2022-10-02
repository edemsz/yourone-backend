package bme.youronebackend.person

import bme.youronebackend.auth.*
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/person")
open class PersonController
 {
     @Autowired
     lateinit var personService: PersonService
     @Autowired
     lateinit var jwtTools: JWTTools


     @GetMapping("/me")
     @ApiOperation("Gets the person entity of the current user")
     fun getMe(@RequestHeader("Authorization") authHeader:String?, )
     :ResponseEntity<Person>{

         val person=personService.getCurrentMember(authHeader,null)
         return ResponseEntity.ok(person)
     }

    @PostMapping("/sign-up")
    @ApiOperation("Sign up method for users")
    fun register(@RequestBody registerData: RegistrationDTO): ResponseEntity<Tokens> {
        personService.register(registerData)

        val loginDTO=LoginDTO(registerData.username,registerData.password)
        return login(loginDTO)
    }

    @PostMapping("/login")
    @ApiOperation("Login method for users")
    fun login(@RequestBody loginData: LoginDTO): ResponseEntity<Tokens> {
        val tokens=personService.login(loginData)
        val jwtCookie=tokens.accessToken
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "jwt-cookie=$jwtCookie").body(tokens)
    }




}