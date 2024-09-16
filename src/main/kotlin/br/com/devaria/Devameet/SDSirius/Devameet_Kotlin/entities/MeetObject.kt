package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

data class MeetObject(
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    val id: Long = 0,
    var name: String = "",

    val x : Int = 0,
    val y : Int = 0,
    val zIndex : Int = 0,
    val orientation : String = "",

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meetId")
    val meet: Meet? = null

) {
}