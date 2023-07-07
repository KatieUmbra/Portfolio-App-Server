package dev.kaytea.portfolio.plugins.routing

import dev.kaytea.portfolio.plugins.routing.authentication.authenticateRouting
import dev.kaytea.portfolio.plugins.routing.authentication.loginRouting
import dev.kaytea.portfolio.plugins.routing.authentication.logoutRouting
import dev.kaytea.portfolio.plugins.routing.authentication.registerRouting
import io.ktor.server.application.*

fun Application.configureAuthRouting() {
    loginRouting()
    logoutRouting()
    registerRouting()
    authenticateRouting()
    sessionRouting()
}