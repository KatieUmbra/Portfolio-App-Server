package dev.kaytea.portfolioTest

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class EnvTest {
    @Test
    fun checkEnvironment() {
        val envVariables = mapOf(
            "Secure database password" to System.getenv("SDP"),
            "Secure salt seed" to System.getenv("SSS"),
            "Secure encryption key" to System.getenv("SEK"),
            "Secure sign key" to System.getenv("SSK")
        )
        envVariables.forEach { (key, value) ->
            println("$key: $value")
            assertNotNull(value)
        }
    }
}