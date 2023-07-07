package dev.kaytea.portfolio.plugins.routing

import dev.kaytea.portfolio.plugins.authentication.eliminateSession
import dev.kaytea.portfolio.plugins.authentication.getSessionsFromId
import dev.kaytea.portfolio.user.UserSession
import dev.kaytea.portfolio.user.UserSessionId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.sessionRouting() {
    routing {
        route("session") {
            authenticate("auth") {
                get("all") {
                    val id = call.sessions.get<UserSessionId>()
                    val sessions = getSessionsFromId(id)
                    if (sessions != null) {
                        call.respond(sessions)
                    } else {
                        eliminateSession(id!!)
                        call.sessions.clear<UserSessionId>()
                        call.respond(
                            status = HttpStatusCode.Forbidden,
                            message = "Couldn't verify session, log in again."
                        )
                    }
                }
                delete("remove/{id}") {
                    val id = call.sessions.get<UserSessionId>()
                    val sessions: List<UserSession>? = getSessionsFromId(id)
                    if (sessions == null) {
                        eliminateSession(id!!)
                        call.sessions.clear<UserSessionId>()
                        call.respond(
                            status = HttpStatusCode.Forbidden,
                            message = "Couldn't verify session, log in again."
                        )
                    }
                    val removedId = call.parameters["id"]?.toIntOrNull()
                    val removedSession = sessions!!.find { it.uniqueId == removedId }
                    if (removedSession == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = "Given ID doesn't exist"
                        )
                    } else {
                        eliminateSession(UserSessionId(removedSession.uniqueId))
                        call.respond(
                            status = HttpStatusCode.Accepted,
                            message = "Session eliminated successfully"
                        )
                    }
                }
            }
        }
    }
}