package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class DefaultResponseMessageDto (val statusCode: Int, val message : Array<String>, val error: String )