package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.persistence.Id


@Entity
data class Meet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0,
    var name: String = "",
    val link: String = "",
    var color: String = "",

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    val user: User? = null,

    @JsonBackReference
    @OneToMany(mappedBy = "meet")
    val objects: List<MeetObject> = emptyList(),

    @JsonBackReference
    @OneToMany(mappedBy = "meetPosition")
    val positions : List<Position> = emptyList()
) {
    override fun toString(): String {
        return "Meet(id=$id, name=$name, link=$link, color=$color)"
    }
}
