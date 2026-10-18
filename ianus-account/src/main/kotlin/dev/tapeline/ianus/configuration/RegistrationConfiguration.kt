package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "account.registration")
data class RegistrationConfiguration(
    val minPasswordLength: Int,
    val enabled: Boolean
)
