package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.filters

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.UserRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JWTAuthorizerFilter(
    authenticationManager: AuthenticationManager,
    val jwtUtils: JWTUtils,
    val userRepository: UserRepository) : BasicAuthenticationFilter(authenticationManager) {
    val authorization = "Authorization"
    val bearer = "Bearer"


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)
        if(authorizationHeader != null && authorizationHeader.startsWith(bearer)){
            val autorizado = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = autorizado
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader.substring(7)
        if (jwtUtils.isTokenValid(token)){
            val idString = jwtUtils.getUserId(token)
            if (!idString.isNullOrEmpty() && !idString.isBlank()){
                val user = userRepository.findByIdOrNull(idString.toLong()) ?: throw UsernameNotFoundException("Usuário não encontrado!")
                val userImpl = UserDetailImpl(user)
                return UsernamePasswordAuthenticationToken(userImpl, null, userImpl.authorities)
            }
        }
        throw UsernameNotFoundException("Token informado inválido")
    }
}