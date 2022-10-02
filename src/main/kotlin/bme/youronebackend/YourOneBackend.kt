package bme.youronebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
open class YourOneBackend{


}
fun main(args: Array<String>) {
    runApplication<YourOneBackend>(*args)
}

