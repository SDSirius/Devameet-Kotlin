package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.DefaultResponseMessageDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


open class BaseController(clazz: String) {

    protected val log = LoggerFactory.getLogger(clazz)

    fun formatErrorResponse(statusCode: HttpStatus, message: Array<String>) : ResponseEntity<Any>
    = ResponseEntity(DefaultResponseMessageDto(statusCode.value(), message, statusCode.toString()),statusCode)


}