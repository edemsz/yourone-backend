package bme.youronebackend.basic

import bme.youronebackend.person.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
open class DataLoader @Autowired constructor
    (
    private var personService: PersonService,
) : ApplicationRunner {


    override fun run(args: ApplicationArguments?) {




        

    }



}