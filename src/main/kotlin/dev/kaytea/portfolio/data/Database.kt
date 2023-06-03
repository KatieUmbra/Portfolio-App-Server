package dev.kaytea.portfolio.data

import dev.kaytea.portfolio.envVars
import io.ktor.server.application.Application
import io.ktor.server.application.log
import org.jetbrains.exposed.sql.Database

fun Application.databaseConnect() {
    Database.connect(
        "jdbc:mysql://192.168.1.20:3306/Portfolio",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root", password = envVars["SDP"]!!
    )
    log.info("Connected to database")
}