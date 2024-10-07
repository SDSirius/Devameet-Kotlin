package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.LoginRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services.LoginService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth/login")
class LoginController(private val loginService:LoginService) : BaseController(LoginController::class.java.toString()) {

    @PostMapping
    fun doLogin( @RequestBody dto:LoginRequestDto): ResponseEntity<Any> {
        try {
            val result = loginService.login(dto)
            return ResponseEntity(result, HttpStatus.OK)

        }catch (bre: BadRequestException){
            return formatErrorResponse(bre.status, bre.messages.toTypedArray())
        }catch (e: Exception){
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu um erro ao efetuar o login").toTypedArray())
        }
    }
}