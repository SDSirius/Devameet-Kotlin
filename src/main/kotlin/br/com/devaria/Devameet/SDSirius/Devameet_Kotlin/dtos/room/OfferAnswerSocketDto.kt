package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

data class OfferAnswerSocketDto(
    val link : String = "",
    val to : String = "",
    var socket : String = "",
    val offer : Any? = null,
    val answer : Any? = null,
)
