package dev.kaytea.portfolio

import dev.kaytea.portfolio.data.databaseConnect
import dev.kaytea.portfolio.plugins.*
import dev.kaytea.portfolio.plugins.authentication.configureAuthentication
import dev.kaytea.portfolio.plugins.authentication.configureSession
import dev.kaytea.portfolio.plugins.routing.configureAuthRouting
import dev.kaytea.portfolio.plugins.routing.configureBlogRouting
import dev.kaytea.portfolio.plugins.routing.configureDataRouting
import dev.kaytea.portfolio.plugins.routing.testRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module)
        .start(wait = true)
}

internal val Application.envVars: Map<String, String>
    get() = mapOf(
        "SDP" to System.getenv("SDP"),
        "SSS" to System.getenv("SSS"),
        "SEK" to System.getenv("SEK"),
        "SSK" to System.getenv("SSK")
    )

fun Application.module() {
    configureBlogRouting()
    configureDataRouting()
    configureAuthRouting()

    configureSerialization()

    configureSession()
    configureAuthentication()

    databaseConnect()

    testRouting()
}
