package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RegisterRequestDto (
    val name: String ,
    val email: String,
    val avatar: String,
    val password: String
)