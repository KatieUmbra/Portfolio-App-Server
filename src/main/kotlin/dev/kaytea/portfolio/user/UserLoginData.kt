package dev.kaytea.portfolio.user

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginData(val identifier: String, val password: String)
