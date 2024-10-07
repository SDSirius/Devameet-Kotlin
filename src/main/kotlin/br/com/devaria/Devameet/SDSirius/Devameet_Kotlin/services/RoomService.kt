package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet.MeetObjectRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.GetRoomResponseDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.LastPositionDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.UpdatePositionRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room.UpdateUserSocketResponseDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.*
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.Throws
import kotlin.random.Random


@Service
class RoomService(
    private val meetRepository: MeetRepository,
    private val meetObjectRepository: MeetObjectRepository,
    private val positionsRepository: PositionsRepository,
    private val userRepository: UserRepository,
    private val lastPositionsRepository: LastPositionsRepository
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

            var newX: Int
            var newY: Int
            var positionOccupied: Boolean

            do {
                newX = Random.nextInt(1, 9)
                newY = Random.nextInt(1, 9)
                positionOccupied = usersInRoom.any { it.x == newX && it.y == newY }
            } while (positionOccupied)

            loggedUserInRoom = Position(
                clientId = dto.clientId,
                userPosition = user,
                meetPosition = meet,
                avatar = user.avatar,
                name = user.name,
                x = newX,
                y = newY,
                orientation = "down",
            )
        }else{
            if(dto.x > 0 && dto.x < 8){
                loggedUserInRoom.x = dto.x
            }

            if(dto.y > 0 && dto.y < 8){
                loggedUserInRoom.y = dto.y
            }

            if (dto.orientation.isNotEmpty() && !dto.orientation.isNullOrBlank()){
                loggedUserInRoom.orientation = dto.orientation
            }
        }
        positionsRepository.save(loggedUserInRoom)
        return loggedUserInRoom
    }

    fun getLasPositionByMeetIdAndLink(userId: Long, link: String) : LastPositionDto?{
        val dto = lastPositionsRepository.findByIdUserIdAndIdLink(userId = userId, link = link)
        if (dto != null){
            val result  = LastPositionDto(
                userId = userId,
                link = link,
                x = dto.x,
                y = dto.y,
                orientation = dto.orientation,
                muted = dto.muted,
            )
            return result
        }
        return null
    }

    fun saveLasPosition(dto: LastPositionDto) {
        log.info("savingLastPositions - start")
        val lastPositionKey = LastPositionKey(
            userId = dto.userId,
            link = dto.link
        )
        val lastPosition = LastPosition(
            id = lastPositionKey,
            x = dto.x,
            y = dto.y,
            orientation = dto.orientation,
            muted = dto.muted
        )

        lastPositionsRepository.save(lastPosition)
        log.info("savingLastPositions - finished")
    }

    fun findByClientId(clientId: String): Position? {
        return positionsRepository.findByClientId(clientId)
    }
}