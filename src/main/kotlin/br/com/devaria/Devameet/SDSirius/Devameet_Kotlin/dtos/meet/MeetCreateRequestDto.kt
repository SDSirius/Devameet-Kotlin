package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeetCreateRequestDto (
    val name: String,
    val color : String
)