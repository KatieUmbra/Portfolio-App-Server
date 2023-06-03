package dev.kaytea.portfolio.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

@Serializable
data class UserData(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

class UserDataDAO(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserDataDAO>(UserDataTable)
    val username by UserDataTable.username
    var password by UserDataTable.hashedPassword
    val email by UserDataTable.email
    val firstName by UserDataTable.firstName
    val lastName by UserDataTable.lastName
}