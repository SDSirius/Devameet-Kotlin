package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.RegisterRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.UserUpdateRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.User
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.UserRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.encrypt
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.isEmailvalid
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.isPasswordvalid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UserService(
    @Value("\${devameet.secrets.aes-secrets}")
    private val secret: String,
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun getUser(userId: Long): User? {
        val optional = userRepository.findById(userId)

        if (optional.isPresent){
            return optional.get()
        }
        return null
    }

    @Throws(BadRequestException::class)
    fun create(dto:RegisterRequestDto){
        log.info("create - start")

        val messages = mutableListOf<String>()

        if(dto.email.isNullOrBlank() || dto.email.isEmpty() ||
            dto.name.isNullOrBlank() || dto.name.isEmpty() ||
            dto.password.isNullOrBlank() || dto.password.isEmpty()){
            messages.add("Favor informar todos os campos obrigatórios.")
            throw BadRequestException(messages)
        }

        if(!isEmailvalid(dto.email)){
            messages.add("Email inválido!")
        }

        if(!isPasswordvalid(dto.password)){
            messages.add("Senha inválida! Senha deve conter ao menos 8 caracteres ")
        }

        if (dto.name.length < 2){
            messages.add("Nome inválido! o nome deve conter ao menos 2 caracteres")
        }

        val existingUser = userRepository.findByEmail((dto.email))

        if(existingUser != null && existingUser.id > 0){
            messages.add("Já existe usuario cadastrado com este email")
        }

        if (messages.size > 0){
            throw BadRequestException(messages)
        }

        val user = User(
            name = dto.name,
            email = dto.email,
            password = encrypt(dto.password, secret),
            avatar = dto.avatar
        )

        userRepository.save(user)
        log.info("Create User - Success!")
    }

    @Throws(BadRequestException::class)
    fun update(user: User ,dto:UserUpdateRequestDto) {
        log.info("update - start")
        val messages = mutableListOf<String>()

        if (!dto.name.isNullOrBlank() && dto.name.length < 2 ){
            messages.add("Nome Inválido")
        }else if (!dto.name.isNullOrEmpty() && !dto.name.isBlank()) {
            user.name = dto.name
        }

        if (!dto.avatar.isNullOrBlank() && dto.avatar.length < 5){
            messages.add("Avatar Inválido")
        }else if (!dto.avatar.isNullOrEmpty() && !dto.avatar.isBlank()){
            user.avatar = dto.avatar
        }

        if (messages.size > 0){
            throw BadRequestException(messages)
        }

        userRepository.save(user)
        log.info("update - finish success")
    }

}