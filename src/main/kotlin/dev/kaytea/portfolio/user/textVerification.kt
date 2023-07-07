package dev.kaytea.portfolio.user

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class ProfanityFilterResponse(
    val original: String,
    val censored: String,
    @Suppress("PropertyName")
    val has_profanity: Boolean
)

fun validateUsername(usr: String): Boolean {
    val reg = Regex("[a-zA-Z0-9_]")
    return usr.matches(reg)
}

fun validatePassword(pass: String): Boolean {
    val reg = Regex("^(?=[^a-z]*[a-z])(?=[^A-Z]*[A-Z])(?=\\D*\\d)(?=[^!#%]*[!#%])[A-Za-z0-9!#%]{8,40}$")
    return pass.matches(reg)
}

fun containsProfanity(str: String): Pair<Boolean?, ProfanityFilterResponse?> {
    val url = URL("https://api.api-ninjas.com/v1/profanityfilter?text=${str.replace(" ", "%20")}")
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("X-Api-Key", System.getenv("NSAK"))
    connection.requestMethod = "GET"
    return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
        val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
        var inputLine = bufferedReader.readLine()
        val response = StringBuffer()
        while (inputLine != null) {
            response.append(inputLine)
            inputLine = bufferedReader.readLine()
        }
        bufferedReader.close()
        val filterResponse = Json.decodeFromString<ProfanityFilterResponse>(response.toString())
        Pair(filterResponse.has_profanity, filterResponse)
    } else Pair(null, null)
}