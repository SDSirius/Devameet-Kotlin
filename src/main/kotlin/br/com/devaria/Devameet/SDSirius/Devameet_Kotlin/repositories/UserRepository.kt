package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories


import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail( email : String ): User?

}