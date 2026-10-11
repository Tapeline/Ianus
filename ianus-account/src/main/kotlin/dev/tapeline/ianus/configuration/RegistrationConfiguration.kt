package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.registration")
data class RegistrationConfiguration(
    val minPasswordLength: Int,
    val enabled: Boolean
)
