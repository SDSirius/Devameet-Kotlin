package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.dtos.room


data class UpdateUsersSocketResponseDto(val users : List<UpdateUserSocketResponseDto>)

data class UpdateUserSocketResponseDto(
    val clientId: String = "",
    val user : Long = 0,
    val name: String = "",
    val avatar: String = "",
    var x: Int = 0,
    var y: Int = 0,
    var orientation: String = "",
    var muted: Boolean = false,
)