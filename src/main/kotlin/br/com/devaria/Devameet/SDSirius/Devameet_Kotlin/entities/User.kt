package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.entities


import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val email: String = "",
    var name: String = "",

    @JsonIgnore
    var password: String = "",
    var avatar: String = "",

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    val meets : List<Meet> = emptyList(),

    @JsonBackReference
    @OneToMany(mappedBy = "userPosition")
    val positions : List<Position> = emptyList()
){
    override fun toString(): String {
        return "User(id=$id, name=$name, email=$email)"
    }
}