package dev.kaytea.portfolio.plugins.routing

import dev.kaytea.portfolio.plugins.authentication.validateLogin
import dev.kaytea.portfolio.plugins.authentication.validateSessionId
import dev.kaytea.portfolio.user.UserData
import dev.kaytea.portfolio.user.UserDataTable
import dev.kaytea.portfolio.user.UserLoginData
import dev.kaytea.portfolio.user.UserSessionId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

fun Application.testRouting() {
    routing {
        route("/test") {
            post("/register") {
                // Receiving json for the serialized UserData class.
                val user = call.receive<UserData>()
                var response = HttpStatusCode.Conflict
                // making sure there are no repeated usernames and emails
                val foundUser = transaction {
                    UserDataTable.select {
                        (UserDataTable.username eq user.username)
                            .or(UserDataTable.email eq user.email)
                    }.firstOrNull()
                }
                if (foundUser == null) {
                    val hashPassword = BCrypt.hashpw(user.password, BCrypt.gensalt(10))
                    val newUser = user.copy(password = hashPassword)
                    response = HttpStatusCode.Created
                    // storing hashed password
                    transaction {
                        UserDataTable.insert { row ->
                            row[username] = newUser.username
                            row[hashedPassword] = newUser.password
                            row[email] = newUser.email
                            row[firstName] = newUser.firstName
                            row[lastName] = newUser.lastName
                        }
                    }
                }
                call.respondText("Finished", status = response)
            }
            post("/login") {
                val user = call.receive<UserLoginData>()
                var response = HttpStatusCode.Unauthorized
                val authId = validateLogin(user)
                if (authId != null) {
                    response = HttpStatusCode.OK
                    call.sessions.set(UserSessionId(authId))
                }
                call.respondText("Finished", status = response)
            }
            get("/auth") {
                val session = call.sessions.get<UserSessionId>()
                var responseCode = HttpStatusCode.BadRequest
                var responseBody = "Unauthorized"
                session?.let {
                    if (validateSessionId(session)) {
                        responseCode = HttpStatusCode.Accepted
                        responseBody = "Authenticated"
                        call.sessions.set(session)
                    }
                }
                call.respondText(text = responseBody, status = responseCode)
            }
            authenticate("auth") {
                get("/protectedRoute") {
                    call.respondText("how did you get here? this is a protected route, wait you are allowed!")
                }
            }
        }
    }
}