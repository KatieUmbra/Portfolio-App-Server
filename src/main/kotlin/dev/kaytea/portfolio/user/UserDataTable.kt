package dev.kaytea.portfolio.user

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UserDataTable: IntIdTable() {
    val username: Column<String> = varchar("username", 50)
    val hashedPassword: Column<String> = varchar("hashedPassword", 200)
    val email: Column<String> = varchar("email", 60)
    val firstName: Column<String> = varchar("firstName", 20)
    val lastName: Column<String> = varchar("lastName", 20)
}