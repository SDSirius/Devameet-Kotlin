package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.repositories

import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.LastPosition
import br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities.LastPositionKey
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LastPositionsRepository : JpaRepository<LastPosition, LastPositionKey> {

    fun findByIdUserIdAndIdLink(
        @Param("userId") userId: Long,
        @Param("link") link: String
    ): LastPosition?

    @Transactional
    @Modifying
    @Query("DELETE FROM LastPosition p WHERE p.id.link = :link")
    fun deleteAllLastPositionsByLink(@Param("link") link: String)
}
