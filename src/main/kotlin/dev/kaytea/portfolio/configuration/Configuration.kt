package dev.kaytea.portfolio.configuration

import kotlinx.serialization.Serializable

private fun validateIp(ip: String): Boolean {
    val ipRegex =
        "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$"
    val regex = Regex(ipRegex)
    return regex.find(ip) != null
}

private fun validatePort(port: String): Boolean {
    val portRegex =
        "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$"
    val regex = Regex(portRegex)
    return regex.find(port) != null
}

@Serializable
data class Configuration(
    val application: App,
    val database: Database
) {
    val isValid: Boolean =
        (validateIp(application.host) || application.host == "localhost")
                && (validateIp(database.host) || database.host == "localhost")
                && validatePort(application.port)
                && validatePort(database.port)
}

@Serializable
data class App(
    val host: String,
    val port: String
)

@Serializable
data class Database(
    val host: String,
    val port: String,
    val name: String
)