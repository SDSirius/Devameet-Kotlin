package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.configs

import com.corundumstudio.socketio.SocketIOServer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.jvm.Throws

@Component
class ServerCommandLineRunner(private val server:SocketIOServer): CommandLineRunner{

    @Throws(Exception::class)
    override fun run(vararg args: String){
        server.start()
    }

}