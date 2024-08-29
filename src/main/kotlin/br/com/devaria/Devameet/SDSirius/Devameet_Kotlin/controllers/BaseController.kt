package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.DefaultResponseMessageDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


open class BaseController {

    fun formatErrorResponse(statusCode: HttpStatus, message: Array<String>) : ResponseEntity<Any>
    = ResponseEntity(DefaultResponseMessageDto(statusCode.value(), message, statusCode.toString()),statusCode)


}