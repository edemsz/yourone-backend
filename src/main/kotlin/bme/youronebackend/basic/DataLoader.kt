package bme.youronebackend.basic

import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom
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
        for (i in 0..500) {
            personService.add(createPerson())
        }
    }

    private fun createPerson(): Person {
        val p = Person("a", "random@gmail.com", "", randomDate())
        val randomValues = List(30) { Random.nextInt(0, 2) }
        if (randomValues[0] == 0)
            p.gender = "Male"
        if (randomValues[0] == 1)
            p.gender = "Female"
        p.city = if (randomValues[1] == 0) "Budapest" else "Szeged"
        p.jobType = if (randomValues[2] == 0) "IT" else "health"
        p.eduLevel = if (randomValues[3] == 0) "University" else "high school"
        p.cigarettes = if (randomValues[4] == 0) "Every day" else "Never"
        p.childrenNumber = if (randomValues[6] == 0) "3+" else "None"
        p.maritalStatus = if (randomValues[7] == 0) "Married" else "Widow"
        p.musicalTaste = if (randomValues[8] == 0) "Hard rock" else "Hip-hop"
        p.filmTaste = if (randomValues[9] == 0) "Romance" else "Sci-fi"
        p.religion = if (randomValues[10] == 0) "Reformed" else "Atheist"
        p.horoscope = if (randomValues[11] == 0) "Pisces" else "Capricorn"
        p.languages = if (randomValues[12] == 0) "Only English" else "English, Hungarian, Basque"
        p.interests = if (randomValues[13] == 0) "Football" else "Cooking"
        p.height = if (randomValues[15] == 0) 170 else 180
        p.tattoo = if (randomValues[16] == 0) "Only one" else "Never"
        p.hairColour = if (randomValues[17] == 0) "Brown" else "Fair"
        p.eyeColour = if (randomValues[18] == 0) "Green" else "Blue"
        p.piercing = if (randomValues[19] == 0) "Only one" else "Never"
        p.glasses = if (randomValues[20] == 0) "Myopic" else "No"
        p.sportiness = if (randomValues[22] == 0) "Every day" else "No sport"
        if (p.gender == "Male")
            p.beard = if (randomValues[22] == 0) "Has long beard" else "None"
        if (p.gender == "Female")
            p.breastSize = if (randomValues[23] == 0) "EE" else "C"
        if (p.gender == "Male")
            p.name = randomMaleName()
        if (p.gender == "Female")
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
        return "Béla"
        val text = File("static/osszesffi.txt").readText()
        val list = text.split("\n")
        return list.random()
    }

    private fun randomFemaleName(): String {
        return "Gizi"

        val text = File("static/osszesnoi.txt").readText()
        val list = text.split("\n")
        return list.random()
    }


}