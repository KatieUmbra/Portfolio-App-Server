package dev.kaytea.portfolio.plugins.authentication

import dev.kaytea.portfolio.user.*
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom

private tailrec fun generateRandom(): Int {
    val myRandom = SecureRandom().nextInt()
    val existsInDatabase = transaction {
        UserSessionDAO.find {
            SessionsTable.uniqueId eq myRandom
        }.firstOrNull()
    } == null
    return if (existsInDatabase) myRandom else generateRandom()
}

fun validateLogin(login: UserLoginData): Int? {
    val databaseUser = transaction {
        UserDataDAO.find {
            (UserDataTable.username eq login.identifier) or
                    (UserDataTable.email eq login.identifier)
        }.firstOrNull()
    }
    if (login.identifier.contains(' ') || databaseUser == null) return null
    return if (BCrypt.checkpw(login.password, databaseUser.password)) {
        val newRandom = generateRandom()
        transaction {
            UserSessionDAO.new {
                uniqueId = newRandom
                identifier = login.identifier
                hashedPassword = databaseUser.password
            }
        }
        newRandom
    } else null
}