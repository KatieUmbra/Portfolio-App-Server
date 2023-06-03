package dev.kaytea.portfolio.plugins.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDataRouting() {
    routing {
        route("/data") {
            get("/{projectName}") {
                TODO("Github api project fetch")
            }
        }
    }
}