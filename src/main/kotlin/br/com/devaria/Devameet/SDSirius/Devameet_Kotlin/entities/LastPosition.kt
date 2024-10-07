package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities

import jakarta.persistence.*

@Entity
data class LastPosition(

    @EmbeddedId
    var id: LastPositionKey = LastPositionKey(0, ""),
    var x: Int = 0,
    var y: Int = 0,
    var orientation: String = "",
    var muted: Boolean = false
)
