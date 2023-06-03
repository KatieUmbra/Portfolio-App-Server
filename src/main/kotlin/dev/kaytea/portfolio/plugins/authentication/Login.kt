package dev.kaytea.portfolio.plugins.authentication

import dev.kaytea.portfolio.envVars
import dev.kaytea.portfolio.user.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom

private fun generateRandom(): Int {
    var myRandom = SecureRandom().nextInt()
    val existsInDatabase = transaction {
        UserSessionDAO.find {
            SessionsTable.uniqueId eq myRandom
        }.firstOrNull()
    } != null
    if (existsInDatabase) myRandom = generateRandom()
    return myRandom
}

fun Application.validateLogin(login: UserLoginData): Int? {
    val databaseUser = transaction {
        UserDataDAO.find {
            (UserDataTable.username eq login.identifier) or
                    (UserDataTable.email eq login.identifier) }.firstOrNull()
    }
    log.debug("found database password: ${databaseUser?.password}")
    if (login.identifier.contains(' ') || databaseUser == null) return null
    val salt = "$2a$15$${envVars["SSS"]!!}"
    log.debug("my Salt: {}", salt)
    val hashPassword = BCrypt.hashpw(login.password, salt)
    log.debug("hashed password: {}", hashPassword)
    return if (databaseUser.password == hashPassword) {
        val newRandom = generateRandom()
        transaction {
            UserSessionDAO.new {
                uniqueId = newRandom
                identifier = login.identifier
                hashedPassword = hashPassword
            }
        }
        newRandom
    }
    else null
}