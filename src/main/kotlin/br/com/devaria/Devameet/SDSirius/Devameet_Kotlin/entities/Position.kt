package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities


import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
data class Position(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0,
    val clientId: String = "",
    val name: String = "",
    val avatar: String = "",
    var x: Int = 0,
    var y: Int = 0,
    var orientation: String = "",
    var muted: Boolean = false,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetId")
    val meetPosition: Meet? = null,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val userPosition: User? = null,
)