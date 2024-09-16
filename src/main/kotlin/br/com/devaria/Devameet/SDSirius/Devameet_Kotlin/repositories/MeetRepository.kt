package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories


import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.Meet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MeetRepository: JpaRepository<Meet, Long> {

    fun findAllByUserId (userId : Long) : List<Meet>

    fun findByLink(link : String) : Meet?

}