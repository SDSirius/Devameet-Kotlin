package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserUpdateRequestDto(val name: String, val avatar : String) {
}