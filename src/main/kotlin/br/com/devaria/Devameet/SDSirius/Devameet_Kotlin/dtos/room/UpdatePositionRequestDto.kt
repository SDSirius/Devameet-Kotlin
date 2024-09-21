package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

data class UpdatePositionRequestDto(
    val userId : Long,
    var link : String,
    val clientId : String = "",
    val x:Int = 0,
    val y:Int = 0,
    val orientation: String = "",
    val muted : Boolean = false
)