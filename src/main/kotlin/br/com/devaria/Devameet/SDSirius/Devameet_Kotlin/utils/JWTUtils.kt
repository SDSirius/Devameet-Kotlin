package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class JWTUtils(
    @Value("\${devameet.secrets.jwt-secrets}")
    private val secretKeyJwt : String
){

    val key = Keys.hmacShaKeyFor(secretKeyJwt.toByteArray())

    fun generateToken(userId:String): String{

        return Jwts.builder().setSubject(userId).signWith(key).compact()
    }

    private fun getClaimsToken(token:String) = try {
        Jwts.parser().setSigningKey(key).parseClaimsJws(token).body
    }catch (e:Exception){
        null
    }

    fun getUserId(token: String): String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }

    fun isTokenValid(token:String):Boolean {
        val userId = getUserId(token)
        if (!userId.isNullOrEmpty() && !userId.isBlank()){
            return true
        }
        return false
    }
}