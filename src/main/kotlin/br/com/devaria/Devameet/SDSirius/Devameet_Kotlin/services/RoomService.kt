package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet.MeetObjectRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.GetRoomResponseDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.UpdatePositionRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.UpdateUserSocketResponseDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.UpdateUsersSocketResponseDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.Position
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.MeetObjectRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.MeetRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.PositionsRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class RoomService(
    private val meetRepository: MeetRepository,
    private val meetObjectRepository: MeetObjectRepository,
    private val positionsRepository: PositionsRepository,
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(RoomService::class.java)

    @Throws(BadRequestException::class)
    fun getRoom(link:String): GetRoomResponseDto{
        log.info("getRoom - start")

        val meet = meetRepository.findByLink(link)
        if(meet == null || meet.id <= 0){
            throw BadRequestException(mutableListOf("Reunião não encontrada!"))
        }

        val meetObjects = meetObjectRepository.findAllByMeetId(meet.id)

        val objects = meetObjects.map { MeetObjectRequestDto(
            name = it.name,
            x = it.x,
            y = it.y,
            orientation = it.orientation,
            zIndex = it.zIndex
        )}
        val result = GetRoomResponseDto(
            name = meet.name,
            color = meet.color,
            link = meet.link,
            objects
        )

        log.info("getRoom - finish success")
        return result
    }

    fun listAllPositions(link: String) : List<UpdateUserSocketResponseDto>{
        val meet = meetRepository.findByLink(link) ?: throw BadRequestException(mutableListOf("Reunião não encontrada!"))
        return positionsRepository.findAllByMeetId(meet.id).map {
            UpdateUserSocketResponseDto(
                clientId = it.clientId,
                user = it.userPosition!!.id ,
                name = it.name,
                avatar = it.avatar,
                x = it.x,
                y = it.y,
                orientation = it.orientation,
                muted = it.muted

            )
        }
    }

    fun deletePositionByClientId(clientId: String){
        return positionsRepository.deleteAllByClientId(clientId)
    }

    fun updateUserMuted(dto:UpdatePositionRequestDto){
        val meet = meetRepository.findByLink(dto.link) ?: throw BadRequestException(mutableListOf("Reunião não encontrada!"))

        val position = positionsRepository.findByClientId(dto.clientId) ?: throw BadRequestException(mutableListOf("Posição não encontrada!"))

        position.muted = dto.muted

        positionsRepository.save(position)
    }

    fun updateUserPosition(
        dto:UpdatePositionRequestDto
    ) : Position {
        val user = userRepository.getReferenceById(dto.userId.toLong())
        val meet = meetRepository.findByLink(dto.link) ?: throw BadRequestException(mutableListOf("Reunião não encontrada!"))

        val usersInRoom = positionsRepository.findAllByMeetId(meet.id)

        var loggedUserInRoom = usersInRoom.find { it.userPosition?.id == user.id || it.clientId == dto.clientId }

        if(loggedUserInRoom == null){
            if(usersInRoom != null && usersInRoom.size > 20){
                throw BadRequestException(mutableListOf("Reunião excedeu a quantidade de participantes"))
            }

            loggedUserInRoom = Position(
                clientId = dto.clientId,
                userPosition = user,
                meetPosition = meet,
                avatar = user.avatar,
                name = user.name,
                x = 2,
                y = 2,
                orientation = "down",
            )
        }else{
            val messages = mutableListOf<String>()

            if (dto.x < 0 || dto.x >8){
                messages.add("Eixo X Inválido!")
            }

            if (dto.y < 0 || dto.y >8){
                messages.add("Eixo Y Inválido!")
            }

            if (dto.orientation.isNullOrEmpty() || dto.orientation.isBlank()){
                messages.add("Orientação Inválida!")
            }

            if(messages.size > 0){
                throw BadRequestException(messages)
            }

            loggedUserInRoom.x = dto.x
            loggedUserInRoom.y = dto.y
            loggedUserInRoom.orientation = dto.orientation
        }

        positionsRepository.save(loggedUserInRoom)
        return loggedUserInRoom
    }

    fun findByClientId(clientId: String): Position? {
        return positionsRepository.findByClientId(clientId)
    }
}