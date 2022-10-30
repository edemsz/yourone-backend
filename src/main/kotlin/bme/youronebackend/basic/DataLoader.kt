package bme.youronebackend.basic

import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
import bme.youronebackend.person.yourone.YourOneEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.roundToInt
import kotlin.random.Random


@Component
open class DataLoader @Autowired constructor(
    private var personService: PersonService,
) : ApplicationRunner {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder


    override fun run(args: ApplicationArguments?) {
        manyPeople()
    }

    fun manyPeople() {

        for (i in 0..1000) {
            val p = personService.add(createPerson(i))
        }
    }

    private fun randomNumber(maxInclusive: Int): Int {
        return Random.nextInt(0, maxInclusive)
    }

    private fun randomNumberList(maxInclusive: Int, listNumber: Int): List<Int> {
        return (0 until maxInclusive).shuffled().take(Random.nextInt(listNumber)).sorted()
    }

    private fun createPerson(i: Int): Person {
        val id = i + 1
        val p = Person("a", "random$id@gmail.com", randomDate())
        p.username = "random$id@gmail.com"
        p.password = passwordEncoder.encode("sziaocsike")
        p.gender = randomNumber(3)
        p.city = randomCity()
        p.jobType = randomNumber(28)
        p.eduLevel = randomNumber(8)
        p.cigarettes = randomNumber(5)
        p.childrenNumber = randomNumber(4)
        p.maritalStatus = randomNumber(5)
        p.alcohol = randomNumber(4)
        p.musicalTaste = randomNumberList(15, 3)
        p.filmTaste = randomNumberList(14, 7)
        p.religion = randomNumber(25)
        p.horoscope = randomNumber(12)
        p.languages = randomNumberList(13, 3)
        p.interests = randomNumberList(12, 4)
        p.height = (java.util.Random().nextGaussian() * 30 + 180).roundToInt()
        p.tattoo = randomNumber(4)
        p.hairColour = randomNumber(7)
        p.eyeColour = randomNumber(5)
        p.piercing = randomNumber(4)
        p.glasses = randomNumber(4)
        p.sportiness = randomNumber(5)
        p.shape = randomNumber(6)
        if (p.gender!! > 0) p.facialHair = randomNumber(3)
        if (p.gender == 0) p.breastSize = randomNumber(7)
        if (p.gender!! > 0) p.name = randomMaleName()
        if (p.gender == 0) p.name = randomFemaleName()
        p.minAge = 18
        p.maxAge = Random.nextInt(25, 36)
        p.chemistry = Random.nextInt(1, 5)
        p.theirOne=makeYourOne(p)

        return p
    }

    private fun makeYourOne(p: Person): YourOneEntity {
        val theirOne=YourOneEntity()
        theirOne.alcohol=p.alcohol
        theirOne.breastSize=p.breastSize
        theirOne.childrenNumber=p.childrenNumber
        theirOne.height=p.height
        theirOne.gender=p.gender
        theirOne.tattoo=p.tattoo
        theirOne.eyeColour=p.eyeColour
        theirOne.hairColour=p.hairColour
        theirOne.piercing=p.piercing
        theirOne.sportiness=p.sportiness
        theirOne.glasses=p.glasses
        theirOne.shape=p.shape
        theirOne.breastSize=p.breastSize
        theirOne.facialHair=p.facialHair
        theirOne.jobType=p.jobType
        theirOne.eduLevel=p.eduLevel
        theirOne.cigarettes=p.cigarettes
        theirOne.musicalTaste=p.musicalTaste
        theirOne.filmTaste=p.filmTaste
        theirOne.religion=p.religion
        theirOne.horoscope=p.horoscope
        theirOne.languages=p.languages
        theirOne.interests=p.interests
        return theirOne
    }

    private fun randomDate(): LocalDate {
        val minDay = LocalDate.of(1996, 1, 1).toEpochDay()
        val maxDay = LocalDate.of(2003, 12, 31).toEpochDay()
        val randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay)
        return LocalDate.ofEpochDay(randomDay)
    }

    private fun randomMaleName(): String {
        val content = getText("osszesffi.txt")


        val list = content.split("\n")
        return list.random()
    }

    private fun randomCity(): String {
        val content = getText("varosok.txt")


        val list = content.split("\n")
        return list.random()
    }

    private fun randomFemaleName(): String {
        val content = getText("osszesnoi.txt")

        val list = content.split("\n")
        return list.random()
    }

    private fun getText(path: String): String {
        return this::class.java.classLoader.getResource(path)!!.readText()
    }

}