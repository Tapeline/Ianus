package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI
import java.time.Duration

@ConfigurationProperties(prefix = "connect")
data class OidcAppsConfiguration(
    val apps: Array<OidcAppConfiguration>,
)

data class OidcAppConfiguration(
    val id: String,
    val name: String,
    val secret: String,
    val scopes: Array<String>,
    val redirectUri: URI,
)
