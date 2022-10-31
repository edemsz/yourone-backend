package bme.youronebackend.person.file

import bme.youronebackend.basic.ResourceNotFoundException
import bme.youronebackend.person.Person
import bme.youronebackend.person.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class StorageService {
    @Autowired
    lateinit var personService: PersonService

    private var rootLocation: Path = Paths.get("src/main/resources/static-files")
    private fun load(filename: String): Path? {
        return rootLocation.resolve(filename)
    }

    fun loadAsResource(filename: String): Resource? {
        return try {
            val file = load(filename)
            val resource: Resource = UrlResource(file!!.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw ResourceNotFoundException()
            }
        } catch (e: MalformedURLException) {
            throw ResourceNotFoundException()
        }
    }


    fun store(file: MultipartFile, person: Person, index: Int? = null): String {
        val randomName = UUID.randomUUID().toString().replace("-", "")

        val newFilename: String =
            randomName + "." + file.originalFilename!!.substring(file.originalFilename!!.lastIndexOf(".") + 1)

        try {
            if (file.isEmpty) {
                throw StorageException("Failed to store empty file.")
            }
            val destinationFile: Path? =
                this.rootLocation.resolve(Paths.get(file.originalFilename!!)).normalize().toAbsolutePath()
            if (destinationFile != null) {
                if (!destinationFile.parent.equals(this.rootLocation.toAbsolutePath())) {
                    throw StorageException("Cannot store file outside current directory.")
                }
            }
            file.inputStream.use { inputStream ->
                Files.copy(inputStream, rootLocation.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            throw StorageException("Failed to store file.", e)
        }
        val filePath = rootLocation.resolve(newFilename).toString()
        personService.savePhoto(person, filePath, index)
        return filePath
    }


}

class StorageException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

