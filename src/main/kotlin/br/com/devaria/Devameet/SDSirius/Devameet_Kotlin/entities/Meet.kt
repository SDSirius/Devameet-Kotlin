package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

data class Meet(
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    val id: Long = 0,
    var name: String = "",
    var color:String= "",
    val link:String= "",

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    val user:User? = null,

    @JsonBackReference
    @OneToMany(mappedBy = "objects")
    val objects: List<MeetObject> = emptyList()
) {
}