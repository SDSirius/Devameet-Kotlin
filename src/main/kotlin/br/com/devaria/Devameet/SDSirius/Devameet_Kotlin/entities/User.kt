package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities

import jakarta.persistence.Entity
import jakarta.persistence.*

@Entity
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    val email: String = "",
    var name: String = "",
    var password: String = "",
    var avatar : String = ""
)