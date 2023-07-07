package dev.kaytea.portfolio.plugins.authentication

import dev.kaytea.portfolio.user.*
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSessionId>("auth", directorySessionStorage(File("build/data/.sessions"))) {
            //cookie.httpOnly = true
            //cookie.secure = true
            cookie.path = "/"
            cookie.extensions["SameSite"] = "strict"
        }
    }
}

fun getSessionsFromId(id: UserSessionId?): List<UserSession>? {
    val session = transaction {
        UserSessionDAO.find {
            SessionsTable.uniqueId eq (id?.id ?: 0)
        }.firstOrNull()
    } ?: return null
    val user = transaction {
        UserDataDAO.find {
            (UserDataTable.email eq session.identifier) or
                    (UserDataTable.username eq session.identifier)
        }.firstOrNull()
    } ?: return null
    return transaction {
        val users = mutableListOf<UserSession>()
        UserSessionDAO.find {
            (SessionsTable.identifier eq user.email) or
                    (SessionsTable.identifier eq user.username)
        }.forEach {
            users += UserSession(it.uniqueId, it.identifier, it.hashedPassword)
        }
        users.toList()
    }
}