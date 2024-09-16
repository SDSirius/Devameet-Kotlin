package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.configs

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.filters.JWTAuthorizerFilter
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.UserRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Autowired
    private lateinit var userRepository: UserRepository

    @Bean
    fun configureHttpSecurity(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { authz: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                authz
                    .requestMatchers(HttpMethod.POST, "/auth/*").permitAll()
                    .anyRequest().authenticated()
            }
            .cors { it.configurationSource(configureCors()) }
            .addFilter(JWTAuthorizerFilter(authenticationManager(http), jwtUtils, userRepository))
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        return http.build()
    }

    @Bean
    fun configureCors(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.addAllowedOriginPattern("*")
        configuration.addAllowedMethod("*")
        configuration.addAllowedHeader("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        try {
            return http.getSharedObject(AuthenticationManager::class.java)
        }catch (e:Exception){
            println(http.getSharedObject(AuthenticationManager::class.java))
            println(e)
            return http.getSharedObject(AuthenticationManager::class.java)
        }
    }
}
