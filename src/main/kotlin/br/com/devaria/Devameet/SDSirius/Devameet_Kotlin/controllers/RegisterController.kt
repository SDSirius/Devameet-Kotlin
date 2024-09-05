package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.RegisterRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth/register")
class RegisterController(val userService: UserService) : BaseController(RegisterController::class.java.toString()) {

    @PostMapping
    fun doCreate( @RequestBody dto: RegisterRequestDto): ResponseEntity<Any> {
        var messages = mutableListOf<String>()
        try {
            userService.create(dto)
            return ResponseEntity(HttpStatus.CREATED)

        }catch (e: BadRequestException){
            return formatErrorResponse(e.status, e.messages.toTypedArray())

        }catch (e: Exception){
            log.error("Ocorreu um erro ao efetuar cadastro.", e)
            messages.add("Ocorreu um erro ao efetuar cadastro.")
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, messages.toTypedArray())
        }
    }
}