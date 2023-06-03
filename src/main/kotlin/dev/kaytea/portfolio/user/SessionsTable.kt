package dev.kaytea.portfolio.user

import org.jetbrains.exposed.dao.id.IntIdTable

object SessionsTable : IntIdTable() {
    val uniqueId = integer("uniqueId")
    val identifier = varchar("identifier", 255)
    val hashedPassword = varchar("hashedPassword", 255)
}