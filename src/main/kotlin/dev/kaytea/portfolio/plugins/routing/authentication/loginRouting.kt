package dev.kaytea.portfolio.plugins.routing.authentication

import dev.kaytea.portfolio.plugins.authentication.validateLogin
import dev.kaytea.portfolio.user.UserLoginData
import dev.kaytea.portfolio.user.UserSessionId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.loginRouting() {
    routing {
        post("login") {
            val userLoginData = call.receive<UserLoginData>()
            val sessionUUID = validateLogin(userLoginData)
            val response =
                if (sessionUUID != null) {
                    call.sessions.set(UserSessionId(sessionUUID))
                    Pair(HttpStatusCode.OK, "Session created.")
                } else Pair(HttpStatusCode.Unauthorized, "Couldn't create session.")
            call.respondText("Login request evaluated. ${response.second}", status = response.first)
        }
    }
}