package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.meet.MeetObjectRequestDto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetRoomResponseDto(
    val name: String,
    val color: String,
    val link : String,
    val objects : List<MeetObjectRequestDto>
) {
}