package dev.tapeline.ianus.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "connect.token")
data class AccessTokenConfiguration(
    val accessTokenLengthBytes: Int,
    val refreshTokenLengthBytes: Int,
    val accessTokenTtl: Duration,
)
