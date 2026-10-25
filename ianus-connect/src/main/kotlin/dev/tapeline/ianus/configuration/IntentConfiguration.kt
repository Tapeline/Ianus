package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "connect.intent")
data class IntentConfiguration(
    val ttl: Duration,
    val signingKey: String,
    val encryptionKey: String,
)
