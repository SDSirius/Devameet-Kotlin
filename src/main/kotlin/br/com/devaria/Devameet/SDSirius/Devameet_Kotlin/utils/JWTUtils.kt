package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class JWTUtils(
    @Value("\${devameet.secrets.jwt-secret}")
    private val secretKeyJwt: String
) {

    fun generateToken(userId: String): String {
        return Jwts.
        builder().
        setSubject(userId).
        signWith(SignatureAlgorithm.HS512, secretKeyJwt.toByteArray())
            .compact()
    }

    private fun getClaimsToken(token: String) = try {
        Jwts.parser().setSigningKey(secretKeyJwt.toByteArray()).parseClaimsJws(token).body
    }catch (ex:Exception){
        null
    }

    fun getUserId(token: String): String?{
        val claims = getClaimsToken(token)
        return claims?.subject
    }

    fun isTokenValid(token: String) : Boolean {
        val userId = getUserId(token)
        if(!userId.isNullOrEmpty() && userId.isNotBlank()){
            return true
        }
        return false
    }
}