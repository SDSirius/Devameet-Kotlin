package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class JWTUtils(
    @Value("\${devameet.secrets.jwt-secret}")
    val secretKeyJwt : String
){
    fun generateToken(userId:String): String{
        println("Generate token => " + secretKeyJwt)
        return Jwts.builder().setSubject(userId)
            .signWith(SignatureAlgorithm.HS512, secretKeyJwt.toByteArray())
            .compact()
    }

    private fun getClaimsToken(token: String) = try {
        println(" getClaimstoken  => " + secretKeyJwt)
        Jwts
            .parser()
            .setSigningKey(secretKeyJwt.toByteArray())
            .parseClaimsJws(token)
            .body
    } catch (exception: Exception) {
        println("Exception null getClaims => " + secretKeyJwt)
        null
    }

    fun getUserId(token:String)  :String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }

    fun isTokenValid(token:String):Boolean {
        println("is token valid => " + secretKeyJwt)
        val claims = getClaimsToken(token)
        if (claims != null) {
            val userId = claims.subject
            if (!userId.isNullOrEmpty() && !userId.isBlank()) {
                return true
            }
        }
        return false
    }
}