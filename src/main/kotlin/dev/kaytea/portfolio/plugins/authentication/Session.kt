package dev.kaytea.portfolio.plugins.authentication

import dev.kaytea.portfolio.envVars
import dev.kaytea.portfolio.user.UserSession
import dev.kaytea.portfolio.user.UserSessionId
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.*
import io.ktor.util.hex

import java.io.File

fun Application.configureSession() {
    install(Sessions) {
        val secretEncryptKey = hex(this@configureSession.envVars["SEK"]!!)
        val secretSignKey = hex(this@configureSession.envVars["SSK"]!!)
        cookie<UserSessionId> ("auth", directorySessionStorage(File("build/data/.sessions"))) {
            //cookie.httpOnly = true
            //cookie.secure = true
            cookie.path = "/"
            cookie.extensions["SameSite"] = "strict"
            //transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
}