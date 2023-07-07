@file:Suppress("unused")

package dev.kaytea.portfolio.plugins.authentication

import dev.kaytea.portfolio.user.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSessionId>("auth") {
            validate { uniqueId ->
                val session = transaction {
                    UserSessionDAO
                        .find { SessionsTable.uniqueId eq uniqueId.id }
                        .firstOrNull()?.let {
                            UserSession(it.uniqueId, it.identifier, it.hashedPassword)
                        }
                }
                this@configureAuthentication.validateSession(session)
            }
            challenge("/login")
        }
    }
}

fun Application.validateSession(session: UserSession?): UserSessionId? {
    val databaseSession = if (session == null) null
    else transaction {
        UserSessionDAO
            .find {
                SessionsTable.identifier eq session.identifier
            }
            .firstOrNull()?.let {
                UserSession(it.uniqueId, it.identifier, it.hashedPassword)
            }
    }
    return if (session == databaseSession) {
        UserSessionId(session!!.uniqueId)
    } else {
        null
    }
}

fun validateSessionId(session: UserSessionId): Boolean {
    val databaseSession = transaction {
        UserSessionDAO.find {
            SessionsTable.uniqueId eq session.id
        }.firstOrNull()?.let { UserSession(it.uniqueId, it.identifier, it.hashedPassword) }
    }
    val userData = transaction {
        UserDataDAO.find {
            (UserDataTable.username eq (databaseSession?.identifier ?: "")) or
                    (UserDataTable.email eq (databaseSession?.identifier ?: ""))
        }.firstOrNull()?.let { UserData(it.username, it.password, it.email, it.firstName, it.lastName) }
    }
    if (databaseSession == null || userData == null) return false
    return if (databaseSession.hashedPassword != userData.password) {
        eliminateSession(session)
        false
    } else true
}

fun eliminateSession(session: UserSessionId) {
    val se = transaction {
        UserSessionDAO.find { SessionsTable.uniqueId eq session.id }.firstOrNull()
    }
    se?.delete()
}