package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.DefaultResponseMessageDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.User
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services.UserService
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.JWTUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


open class BaseController(clazz: String) {

    protected val log = LoggerFactory.getLogger(clazz)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    fun formatErrorResponse(statusCode: HttpStatus, message: Array<String>) : ResponseEntity<Any>
    = ResponseEntity(DefaultResponseMessageDto(statusCode.value(), message, statusCode.toString()),statusCode)

    fun readToken(authorization: String) : User{
        val token = authorization.substring(7)
        val userIdStr = jwtUtils.getUserId(token)

        if (userIdStr == null || userIdStr.isEmpty()){
            throw IllegalArgumentException("Você não tem permissão para isso!")

        }

        val user = userService.getUser(userIdStr.toLong())
            ?: throw IllegalArgumentException("Você não tem permissão para isso!")

        return user
    }
}