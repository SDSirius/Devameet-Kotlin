package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true )
data class LoginResponseDto (
    val name: String,
    val email: String,
    val token: String
)