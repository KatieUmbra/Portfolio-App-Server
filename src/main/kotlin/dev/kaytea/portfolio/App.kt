package dev.kaytea.portfolio

import com.akuleshov7.ktoml.file.TomlFileReader
import dev.kaytea.portfolio.configuration.Configuration
import dev.kaytea.portfolio.data.databaseConnect
import dev.kaytea.portfolio.plugins.authentication.configureAuthentication
import dev.kaytea.portfolio.plugins.authentication.configureSession
import dev.kaytea.portfolio.plugins.configureSerialization
import dev.kaytea.portfolio.plugins.routing.configureAuthRouting
import dev.kaytea.portfolio.plugins.routing.configureBlogRouting
import dev.kaytea.portfolio.plugins.routing.configureDataRouting
import dev.kaytea.portfolio.plugins.routing.testRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.decodeFromString

private var settings: Configuration? = null

fun main() {
    settings = object {}.javaClass.classLoader.getResource("configuration.toml")
        ?.let { TomlFileReader.decodeFromString<Configuration>(it.readText()) }
    println(settings)
    embeddedServer(
        Netty,
        port = settings!!.application.port.toInt(),
        host = settings!!.application.host,
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    with(settings!!.database) { databaseConnect(host, port, name) }

    configureBlogRouting()
    configureDataRouting()
    configureAuthRouting()

    configureSerialization()

    configureSession()
    configureAuthentication()

    testRouting()
}
