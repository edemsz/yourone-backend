package bme.youronebackend.basic

import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.roundToInt
import kotlin.random.Random

@Component
open class DataLoader @Autowired constructor
    (
    private var personService: PersonService,
) : ApplicationRunner {


    override fun run(args: ApplicationArguments?) {
        manyPeople()
    }

    fun manyPeople() {
        return
        for (i in 0..500) {
            personService.add(createPerson())
        }
    }

    private fun randomNumber(maxInclusive: Int): Int {
        return Random.nextInt(0, maxInclusive )
    }

    private fun createPerson(): Person {
        val p = Person("a", "random@gmail.com", "", randomDate())
        p.gender = randomNumber(3)
        p.city =randomCity()
        p.jobType = randomNumber(28)
        p.eduLevel = randomNumber(8)
        p.cigarettes = randomNumber(5)
        p.childrenNumber = randomNumber(4)
        p.maritalStatus = randomNumber(5)
        p.alcohol = randomNumber(4)
        p.musicalTaste = randomNumber(15)
        p.filmTaste = randomNumber(14)
        p.religion = randomNumber(25)
        p.horoscope = randomNumber(12)
        p.languages = randomNumber(13)
        p.interests = randomNumber(12)
        p.height = (java.util.Random().nextGaussian() * 60 + 150).roundToInt()
        p.tattoo = randomNumber(4)
        p.hairColour = randomNumber(7)
        p.eyeColour = randomNumber(5)
        p.piercing = randomNumber(4)
        p.glasses = randomNumber(4)
        p.sportiness = randomNumber(5)
        p.shape = randomNumber(6)
        if (p.gender!! >0 )
            p.facialHair = randomNumber(3)
        if (p.gender == 1)
            p.breastSize = randomNumber(7)
        if (p.gender!! >0)
            p.name = randomMaleName()
        if (p.gender == 0)
            p.name = randomFemaleName()
        return p
    }

    private fun randomDate(): LocalDate {
        val minDay = LocalDate.of(1996, 1, 1).toEpochDay()
        val maxDay = LocalDate.of(2003, 12, 31).toEpochDay()
        val randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay)
        return LocalDate.ofEpochDay(randomDay)
    }

    private fun randomMaleName(): String {
        val inputStream : InputStream = FileInputStream("static/osszesffi.txt")
        val text = inputStream.bufferedReader(Charsets.ISO_8859_1).use { it.readText()}
        val list = text.split("\n")
        return list.random()
    }

    private fun randomCity(): String {
        val inputStream : InputStream = FileInputStream("static/varosok.txt")
        val text = inputStream.bufferedReader(Charsets.UTF_8).use { it.readText()}
        val list = text.split("\n")
        return list.random()
    }

    private fun randomFemaleName(): String {

        val inputStream : InputStream = FileInputStream("static/osszesnoi.txt")
        val text = inputStream.bufferedReader(Charsets.ISO_8859_1).use { it.readText()}
        val list = text.split("\n")
        return list.random()
    }


}