package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.LoginRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.LoginResponseDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.UserRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.JWTUtils
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.decrypt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class LoginService(

    @Value("\${devameet.secrets.aes-secret")
    private val secret : String,
    private val userRepository: UserRepository
){
    private val log = LoggerFactory.getLogger(LoginService::class.java)

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Throws(BadRequestException::class)
    fun login (dto : LoginRequestDto): LoginResponseDto{
        val messages = mutableListOf<String>()
        log.info("Login - start")

        if(dto.login.isNullOrBlank() || dto.login.isEmpty() ||
            dto.password.isNullOrBlank() || dto.password.isEmpty()){
            messages.add("Favor preencher os campos.")
            throw BadRequestException(messages)
        }

        val existingUser = userRepository.findByEmail(dto.login)

        if (existingUser==null){
            messages.add("Usuário e senha não encontrados.")
            throw BadRequestException(messages)
        }

        val passwordDecripted = decrypt(existingUser.password, secret)

        if (passwordDecripted != dto.password){
            messages.add("Usuário e senha não encontrados.")
            throw BadRequestException(messages)
        }
        val token =jwtUtils.generateToken((existingUser.id.toString()))

        val response = LoginResponseDto(existingUser.name, existingUser.email, token )

        log.info("Login - finished Succes!")
        return response
    }
}