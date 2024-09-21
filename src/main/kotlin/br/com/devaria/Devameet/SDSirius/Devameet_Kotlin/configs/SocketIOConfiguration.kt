package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.configs

import com.corundumstudio.socketio.SocketIOServer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SocketIOConfiguration {

    @Value("\${socket-server.host}")
    private val host : String = ""

    @Value("\${socket-server.port}")
    private val port : Int = 0

    @Bean
    fun SocketIOServer():SocketIOServer{
        val config = com.corundumstudio.socketio.Configuration()
        config.hostname = host
        config.port = port
        return SocketIOServer(config)
    }
}

