package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.Position
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PositionsRepository :JpaRepository<Position, Long> {

    fun findByClientId(clientId: String ): Position?

    @Modifying
    @Query("SELECT p FROM Position p WHERE p.meetPosition.id = :id")
    fun findAllByMeetId(@Param("id") meetId:Long ): List<Position>

    @Transactional
    @Modifying
    @Query("DELETE FROM Position p WHERE p.clientId = :id")
    fun deleteAllByClientId(@Param("id") clientId: String )

}