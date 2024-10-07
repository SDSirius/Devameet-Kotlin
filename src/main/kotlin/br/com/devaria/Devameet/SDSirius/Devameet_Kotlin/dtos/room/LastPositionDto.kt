package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

data class LastPositionDto(
    val userId : Long,
    val link: String,
    var x:Int = 0,
    var y:Int = 0,
    var orientation: String = "",
    var muted : Boolean = false
)
