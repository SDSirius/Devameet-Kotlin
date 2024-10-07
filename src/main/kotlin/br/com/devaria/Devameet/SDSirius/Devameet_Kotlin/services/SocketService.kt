package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.*
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.Position
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.ConnectListener
import com.corundumstudio.socketio.listener.DataListener
import com.corundumstudio.socketio.listener.DisconnectListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SocketService(
    server: SocketIOServer,
    private val roomService: RoomService
){
    private val server : SocketIOServer
    private val log = LoggerFactory.getLogger(SocketService::class.java)

    init{
        this.server = server
        this.server.addConnectListener(onConnected())
        this.server.addDisconnectListener(onDisconnected())
        this.server.addEventListener("join", JoinRoomRequestDto::class.java, onJoin())
        this.server.addEventListener("move", MoveSocketRequestDto::class.java, onMove())
        this.server.addEventListener("toggl-mute-user", ToggleMuteSocketRequestDto::class.java, onTogglMute())
        this.server.addEventListener("call-user", OfferAnswerSocketDto::class.java, onCallUser())
        this.server.addEventListener("make-answer", OfferAnswerSocketDto::class.java, onMakeAnswer())
    }

    private fun onTogglMute(): DataListener<ToggleMuteSocketRequestDto>? {
        return DataListener { client, data, ackSender ->
            val dto = UpdatePositionRequestDto(
                userId = data.userId,
                clientId = client.sessionId.toString(),
                link = data.link,
                muted = data.muted
            )
            roomService.updateUserMuted(dto)
            val users = roomService.listAllPositions(data.link)
            sendToAllClients("${data.link}-update-user-list", client, UpdateUsersSocketResponseDto(users))
        }
    }

    private fun onMove(): DataListener<MoveSocketRequestDto>? {
        return DataListener { client, data, ackSender ->
            val dto = UpdatePositionRequestDto(
                userId = data.userId,
                clientId = client.sessionId.toString(),
                link = data.link,
                x = data.x,
                y = data.y,
                orientation = data.orientation
            )

            roomService.updateUserPosition(dto)
            val users = roomService.listAllPositions(data.link)
            sendToAllClients("${data.link}-update-user-list", client, UpdateUsersSocketResponseDto(users))
        }
    }

    private fun onJoin(): DataListener<JoinRoomRequestDto>? {
        return DataListener { client, data, ackSender ->
            log.info(" ClientId ${client.sessionId} Joining at link ${data.link}")
            val position = roomService.findByClientId(client.sessionId.toString())
            var result: Position? = null
            if(position == null){
                data.clientId = client.sessionId.toString()

                val lastPosition = roomService.getLasPositionByMeetIdAndLink(data.userId, data.link)

                if (lastPosition != null) {
                    log.info("Socket client: Previous position found to client " +
                            "${client.sessionId} @ link ${data.link}")
                    result = roomService.updateUserPosition(UpdatePositionRequestDto(
                        userId = data.userId,
                        clientId = client.sessionId.toString(),
                        link = data.link,
                        x = lastPosition.x,
                        y = lastPosition.y,
                        orientation = lastPosition.orientation,
                        muted = lastPosition.muted
                    ))
                } else {
                    log.info("Socket client: No previous position found to client" +
                            " ${client.sessionId} @ link ${data.link}")
                    val dto = UpdatePositionRequestDto(
                        userId = data.userId,
                        clientId = client.sessionId.toString(),
                        link = data.link
                    )
                    result = roomService.updateUserPosition(dto)
                }

                if(result != null){
                    client.joinRoom(data.link)
                    sendToOthersClient("${data.link}-add-user",
                        client, JoinRoomSocketResponseDto(client.sessionId.toString()))
                }
            }

            val users = roomService.listAllPositions(data.link)
            sendToAllClients("${data.link}-update-user-list", client,
                UpdateUsersSocketResponseDto(users))
            log.info("Socket client: ${client.sessionId} started to join room ${data.link}")
        }
    }

    private fun onMakeAnswer(): DataListener<OfferAnswerSocketDto>? {
        return DataListener { client, data, ackSender ->
            log.info("onMakeAnswer: ${client.sessionId} to: ${data.to}")
            data.socket = client.sessionId.toString()
            sendToSpecificClient("answer-made", client, data)
        }
    }

    private fun onCallUser(): DataListener<OfferAnswerSocketDto>? {
        return DataListener { client, data, ackSender ->
            log.info("onCallUser: ${client.sessionId} to: ${data.to}")
            data.socket = client.sessionId.toString()
            sendToSpecificClient("call-made", client, data)
        }
    }

    private fun onDisconnected(): DisconnectListener? {
        return DisconnectListener { client ->
            log.info("Disconecting Client ${client.sessionId.toString()}")
            val position = roomService.findByClientId(client.sessionId.toString())
            if (position != null) {
                var dto = LastPositionDto(
                    userId = position.userPosition!!.id,
                    link = position.meetPosition!!.link,
                    x = position.x,
                    y = position.y,
                    muted = position.muted,
                    orientation = position.orientation
                )
                roomService.saveLasPosition(dto)
                roomService.deletePositionByClientId(client.sessionId.toString())
                sendToAllClients("${position.meetPosition.link}-remove-user", client, DisconnectedRoomSocketResponseDto(client.sessionId.toString()))
                log.info("ClientId ${client.sessionId} has disconnected to Socket at position x: ${position.x} y: ${position.y}")
            } else {
                log.info("ClientId ${client.sessionId} has disconnected to Socket with null Position")
            }
        }
    }

    private fun onConnected(): ConnectListener? {
        return ConnectListener { client ->

            log.info("ClientId ${client.sessionId} connected to socket")

        }
    }

    private fun sendToAllClients(eventName: String, senderClient: SocketIOClient, data: Any) {
        for(client in senderClient.namespace.getRoomOperations("").clients){
            client.sendEvent(eventName, data)
        }
    }

    private fun sendToSpecificClient(eventName: String, senderClient: SocketIOClient, data: OfferAnswerSocketDto) {
        val clientToSend = senderClient.namespace.getRoomOperations("").clients.find {
            it.sessionId.toString() == data.to
        }
        clientToSend?.sendEvent(eventName, data)
    }

    private fun sendToOthersClient(eventName: String, senderClient: SocketIOClient, data: Any) {
        for(client in senderClient.namespace.getRoomOperations("").clients){
            if(client.sessionId != senderClient.sessionId){
                client.sendEvent(eventName, data)
            }
        }
    }


}