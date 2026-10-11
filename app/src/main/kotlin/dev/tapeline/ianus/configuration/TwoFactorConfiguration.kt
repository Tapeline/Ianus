package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app.2fa")
data class TwoFactorConfiguration(
    val verificationTtl: Duration,
    val persistentTtl: Duration,
    val signingKey: String,
    val encryptionKey: String,
)
