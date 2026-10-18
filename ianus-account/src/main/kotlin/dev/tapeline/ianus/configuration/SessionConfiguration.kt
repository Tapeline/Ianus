package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "account.sessions")
data class SessionConfiguration(
    val minSessionTtl: Duration,
    val maxSessionTtl: Duration,
    val defaultSessionTtl: Duration,
    val sessionIdLengthBytes: Int
)
