package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

data class ToggleMuteSocketRequestDto(
    val userId : Long = 0,
    var link : String = "",
    val muted : Boolean = false
)
