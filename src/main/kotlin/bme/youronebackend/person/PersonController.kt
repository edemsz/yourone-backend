package bme.youronebackend.person

import bme.youronebackend.auth.LoginDTO
import bme.youronebackend.auth.RegistrationDTO
import bme.youronebackend.auth.Tokens
import bme.youronebackend.person.file.StorageService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/person")
open class PersonController {
    @Autowired
    lateinit var personService: PersonService

    @Autowired
    lateinit var storageService: StorageService


    @Autowired
    lateinit var personMapper: PersonMapperFacade


    @GetMapping("/me")
    @ApiOperation("Gets the person entity of the current user")
    fun getMe(@RequestHeader("Authorization") authHeader: String?): ResponseEntity<PersonAllDTO> {

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
    fun changeUserData(
        @RequestBody newData: CreatePersonDTO,
        @PathVariable(value = "id") id: Long,
    ): ResponseEntity<PersonAllDTO> {
        val newPerson = personMapper.allDtoToEntity(newData)
        val changedPerson = personService.updateById(id, newPerson)
        val dto = personMapper.entityToDto(changedPerson)
        return ResponseEntity.ok(dto)
    }


    @GetMapping("/potential-partners")
    fun getPotentialPartner(@RequestHeader("Authorization") authHeader: String?): ResponseEntity<List<PersonAllDTO>> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)
        val shownPeople = personService.getNextPersons(swipingPerson)
        val dto = shownPeople.map { personMapper.pairToDto(it, swipingPerson) }
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/")
    fun getAll(): ResponseEntity<List<PersonAllDTO>> {
        return ResponseEntity.ok(personService.getAll().map { personMapper.entityToDto(it) })
    }

    @PostMapping("/partner-match/no")
    fun noMatch(
        @RequestBody partnerId: Long,
        @RequestHeader("Authorization") authHeader: String?,
    ): ResponseEntity<Boolean> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(personService.noMatch(swipingPerson, partnerId))
    }

    @PostMapping("/partner-match/yes")
    fun yesMatch(
        @RequestBody partnerId: Long,
        @RequestHeader("Authorization") authHeader: String?,
    ): ResponseEntity<Boolean> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)

        return ResponseEntity.ok(personService.yesMatch(swipingPerson, partnerId))

    }

    @GetMapping("/all-partners")
    fun getAllPartners(@RequestHeader("Authorization") authHeader: String?): ResponseEntity<List<PersonAllDTO>> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)
        val matches = personService.getMatches(swipingPerson)
        return ResponseEntity.ok(matches.map { personMapper.pairToDto(it, swipingPerson) })
    }

    @PostMapping("/upload")
    fun uploadFile(
        @RequestHeader("Authorization") authHeader: String?,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<String> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)
        return ResponseEntity.ok(storageService.store(file, swipingPerson))
    }

    @ApiOperation("Uploads the picture to the index-th place of the pictures")
    @PostMapping("/upload/{index}")
    fun uploadFile(
        @RequestHeader("Authorization") authHeader: String?,
        @RequestParam("file") file: MultipartFile,
        @PathVariable("index") index: Int,
    ): ResponseEntity<String> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)
        return ResponseEntity.ok(storageService.store(file, swipingPerson, index))
    }

    @PutMapping("/change-picture-oder/{oldNumber}/{newNumber}")
    @ApiOperation("Moves the oldNumber-th picture to the newNumber-th place in the pictures' list")
    fun changePictureOrder(
        @RequestHeader("Authorization") authHeader: String?,
        @PathVariable("oldNumber") oldNumber: Int,
        @PathVariable("newNumber") newNumber: Int,
    ): ResponseEntity<String> {
        val swipingPerson = personService.getCurrentMember(authHeader, null)
        return ResponseEntity.ok(personService.changePictureOrder(swipingPerson, oldNumber, newNumber))
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    open fun serveFile(@PathVariable filename: String?): ResponseEntity<Resource?>? {
        val file: Resource? = storageService.loadAsResource(filename!!)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file?.filename.toString() + "\"")
            .body<Resource?>(file)
    }


}