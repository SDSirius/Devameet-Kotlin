package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

data class JoinRoomRequestDto(
    val userId : Long = 0,
    val link : String = "",
    var clientId : String = ""
)