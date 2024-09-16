package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeetObjectRequestDto(
    val name:String,

    val x: Int,
    val y: Int,
    val zIndex: Int,
    val orientation:String,

)