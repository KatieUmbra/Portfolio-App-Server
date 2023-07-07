package dev.kaytea.portfolio.data

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.databaseConnect(host: String, port: String, name: String) {
    Database.connect(
        // Name: Portfolio
        "jdbc:mysql://$host:$port/$name",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root", password = System.getenv("SDP")
    )
    log.info("Connected to database")
}