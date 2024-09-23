package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

data class MoveSocketRequestDto(
    val userId : Long = 0,
    var link : String = "",
    val x:Int = 0,
    val y:Int = 0,
    val orientation: String = "",
)