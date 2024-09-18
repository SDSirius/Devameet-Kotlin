package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.services

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet.MeetCreateRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet.MeetObjectRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet.MeetUpdateRequestDto
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.Meet
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.MeetObject
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.User
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.exceptions.BadRequestException
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.MeetObjectRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories.MeetRepository
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils.generateLink
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class MeetService(
    private val meetRepository: MeetRepository,
    private val meetObjectRepository: MeetObjectRepository,
){
    private val log = LoggerFactory.getLogger(LoginService::class.java)

    fun getUserMeets(user: User):List<Meet>{
        return meetRepository.findAllByUserId(user.id)
    }

    fun getMeetObjects(meetId:Long): List<MeetObject>{
        return meetObjectRepository.findAllByMeetId(meetId)
    }

    @Throws(BadRequestException::class)
    fun delete (user: User, meetId:Long){
        val meet = meetRepository.getReferenceById(meetId)
        if (meet != null && meet.user?.id == user.id){
            meetObjectRepository.deleteAllByMeetId(meetId)
            return meetRepository.delete(meet)
        }
        throw BadRequestException(mutableListOf("Meet não encontrada"))
    }

    @Throws(BadRequestException::class)
    fun create (user: User ,dto : MeetCreateRequestDto){
        val messages = mutableListOf<String>()
        log.info("create - start")

        if(dto.name.isNullOrBlank() || dto.name.isEmpty() ||
            dto.color.isNullOrBlank() || dto.color.isEmpty()){
            messages.add("Favor preencher os campos.")
            throw BadRequestException(messages)
        }

        if(dto.name.length<2){
            messages.add("Nome inválido")
        }

        if(dto.color.length<3){
            messages.add("Cor inválida")
        }

        if(messages.size>0){
            throw BadRequestException(messages)
        }

        val meet = Meet(
            name = dto.name,
            color = dto.color,
            user = user,
            link = generateLink()
        )

        meetRepository.save(meet)
        log.info("create - finish success")
    }

    @Throws(BadRequestException::class)
    fun update (user: User, meetId:Long ,dto : MeetUpdateRequestDto){
        val messages = mutableListOf<String>()
        log.info("update - start")

        val meet = meetRepository.getReferenceById(meetId)
        if(meet == null || meet.user?.id != user.id){
            messages.add("Meet não encontrada.")
            throw BadRequestException(messages)
        }

        if(dto.name.isNotBlank() && dto.name.length < 2){
            messages.add("Nome inválido")
        }else if(dto.name.isNotBlank()) {
            meet.name = dto.name
        }

        if(dto.color.isNotBlank() && dto.color.length < 3){
            messages.add("Cor inválida")
        }else if(dto.color.isNotBlank()) {
            meet.color = dto.color
        }

        var obj  : MeetObjectRequestDto
        for (i in 0 until  dto.objects.size){
            obj = dto.objects[i]

            if(obj.name.isNullOrBlank() || obj.name.length < 2){
                messages.add("Nome do objeto inválido na posição: $i ")
            }
            if(obj.x < 0 || obj.x > 8){
                messages.add("Eixo X do objeto inválido na posição: $i ")
            }
            if(obj.y < 0 || obj.y > 8){
                messages.add("Eixo Y do objeto inválido na posição: $i ")
            }
            if(obj.zIndex < 0){
                messages.add("ZIndex do objeto inválido na posição: $i ")
            }
            if(obj.orientation.isNullOrBlank() || obj.orientation.length < 2){
                messages.add("Orientação do objeto inválido na posição: $i ")
            }
        }

        if(messages.size>0){
            throw BadRequestException(messages)
        }

        meetRepository.save(meet)
        meetObjectRepository.deleteAllByMeetId(meet.id)

        var meetObj: MeetObject
        dto.objects.forEach{
            meetObj = MeetObject(
                meet = meet,
                name = it.name,
                x = it.x,
                y = it.y,
                zIndex = it.zIndex,
                orientation = it.orientation
            )
            meetObjectRepository.save(meetObj)
        }
        log.info("update - finish success")
    }
}
