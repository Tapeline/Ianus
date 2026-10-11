package dev.tapeline.ianus.domain.twofactor

import dev.tapeline.ianus.domain.user.UserId
import java.time.Instant
import java.time.Duration

data class SecondFactor(
    val userId: UserId,
    val issuedAt: Instant,
    val ttl: Duration
) {
    val expiresAt get() = issuedAt + ttl
}
