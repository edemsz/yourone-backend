package bme.youronebackend.auth

import bme.youronebackend.person.Person
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate

class User (
    private var username: String? = null,
    var name: String? = null,
    private var password: String? = null,
    var email: String? = null,
    var lastPasswordResetDate: LocalDate? = null) : UserDetails {

    @Transient
    var grantedAuthorities: MutableCollection<out GrantedAuthority>? = null

    override fun getAuthorities() = when (grantedAuthorities) {
        null -> mutableListOf()
        else -> grantedAuthorities!!
    }

    override fun getPassword()=password

    override fun getUsername()=username

    override fun isAccountNonExpired()=true

    override fun isAccountNonLocked()=true

    override fun isCredentialsNonExpired()=true

    override fun isEnabled() =true

    fun setUsername(username: String?) {
        this.username = username
    }
    fun setPassword(password:String?){
        this.password=password
    }
    fun fromPerson(person: Person):User{
        this.password=person.password
        this.username=person.username
        this.email=person.email
        this.name=person.name
        return this
    }
}