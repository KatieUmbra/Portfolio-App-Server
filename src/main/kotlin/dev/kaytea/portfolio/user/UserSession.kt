package dev.kaytea.portfolio.user

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

@Serializable
@JvmInline
value class UserSessionId(val id: Int) : Principal

@Serializable
data class UserSession(
    val uniqueId: Int,
    val identifier: String,
    val hashedPassword: String
) : Principal

class UserSessionDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserSessionDAO>(SessionsTable)

    var uniqueId by SessionsTable.uniqueId
    var identifier by SessionsTable.identifier
    var hashedPassword by SessionsTable.hashedPassword
}

