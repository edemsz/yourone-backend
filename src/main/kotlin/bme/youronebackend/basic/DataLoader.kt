package bme.youronebackend.basic

import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
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
            val p=personService.add(createPerson(i))
        }
    }

    private fun randomNumber(maxInclusive: Int): Int {
        return Random.nextInt(0, maxInclusive)
    }

    private fun randomNumberList(maxInclusive: Int, listNumber: Int): List<Int> {
        return (0 until maxInclusive).shuffled().take(Random.nextInt(listNumber)).sorted()
    }

    private fun createPerson(i:Int): Person {
        val id=i+1
        val p = Person("a", "random$id@gmail.com", "", randomDate())
        p.username="random$id@gmail.com"
        p.password=passwordEncoder.encode("sziaocsike")
        p.gender = randomNumber(3)
        p.city = randomCity()
        p.jobType = randomNumber(28)
        p.eduLevel = randomNumber(8)
        p.cigarettes = randomNumber(5)
        p.childrenNumber = randomNumber(4)
        p.maritalStatus = randomNumber(5)
        p.alcohol = randomNumber(4)
        p.musicalTaste =randomNumberList(15,3)
        p.filmTaste = randomNumberList(14,7)
        p.religion = randomNumber(25)
        p.horoscope = randomNumber(12)
        p.languages = randomNumberList(13,3)
        p.interests = randomNumberList(12,4)
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
        p.minAge=18
        p.maxAge=Random.nextInt(25,36)
        p.chemistry=Random.nextInt(10,40)

        return p
    }

    private fun randomDate(): LocalDate {
        val minDay = LocalDate.of(1996, 1, 1).toEpochDay()
        val maxDay = LocalDate.of(2003, 12, 31).toEpochDay()
        val randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay)
        return LocalDate.ofEpochDay(randomDay)
    }

    private fun randomMaleName(): String {
        val content = getText("osszesffi.txt")


        val text = content// file.bufferedReader(Charsets.ISO_8859_1).use { it.readText()}
        val list = text.split("\n")
        return list.random()
    }

    private fun randomCity(): String {
        val content = getText("varosok.txt")


        val text = content//file.bufferedReader(Charsets.UTF_8).use { it.readText() }
        val list = text.split("\n")
        return list.random()
    }

    private fun randomFemaleName(): String {
        val content = getText("osszesnoi.txt")

        val text = content//file.bufferedReader(Charsets.ISO_8859_1).use { it.readText()}
        val list = text.split("\n")
        return list.random()
    }

    fun getText(path: String): String {
        return this::class.java.classLoader.getResource(path)!!.readText()
    }

}