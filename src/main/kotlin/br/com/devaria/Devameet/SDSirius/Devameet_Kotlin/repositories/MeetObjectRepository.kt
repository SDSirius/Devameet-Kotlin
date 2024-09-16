package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.MeetObject
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MeetObjectRepository: JpaRepository<MeetObject, Long> {

    fun findAllByMeetId (meetId : Long) : List<MeetObject>

    @Transactional
    @Modifying
    @Query("DELETE FROM MeetObject o WHERE o.meet.id = :id")
    fun deleteAllByMeetId(@Param("id") meetId : Long)

}