package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HelloController {

    @GetMapping
    fun hello() :String {
        return "Olá Mundo, devaria 2024 KOTLIN!"
    }
}