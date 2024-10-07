package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class LastPositionKey(
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "link")
    val link: String
    ):Serializable
{
    constructor() : this(0, "")
}