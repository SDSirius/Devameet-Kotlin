package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.controllers

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services.RoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/room")
class RoomController(
    private val roomService: RoomService
):BaseController(RoomController::class.java.toString()) {

    @GetMapping("/{link}")
    fun getRoom (
        @RequestHeader("Authorization") authorization:String,
        @PathVariable link: String
    ): ResponseEntity<Any>{
        try {
            val result = roomService.getRoom(link)
            return ResponseEntity(result, HttpStatus.OK)
        }catch (bre:BadRequestException){
            return formatErrorResponse(bre.status, bre.messages.toTypedArray())
        }catch (e:Exception){
            log.error("Ocorreu erro ao buscar reunião",e)
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao buscar reunião").toTypedArray())
        }
    }
}