package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.LoginRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth/login")
class LoginController : BaseController() {

    @PostMapping
    fun doLogin( @RequestBody dto:LoginRequestDto): ResponseEntity<Any> {
        var messages = mutableListOf<String>()
        try {
            if(dto.login.isNullOrBlank() || dto.login.isEmpty() ||
                dto.password.isNullOrBlank() || dto.password.isEmpty()){
                messages.add("Favor preencher os campos.")
                return formatErrorResponse(HttpStatus.BAD_REQUEST, messages.toTypedArray())
            }

            if (dto.login == "teste@teste.com" && dto.password == "teste@1234"){
                return ResponseEntity(dto, HttpStatus.OK)
            }

            messages.add("Usuário ou senha inválidos.")
            return formatErrorResponse(HttpStatus.BAD_REQUEST, messages.toTypedArray())
        }catch (e: Exception){
            messages.add("Ocorreu um erro ao efetuar Login. Tente novamente.")
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, messages.toTypedArray())
        }
    }
}