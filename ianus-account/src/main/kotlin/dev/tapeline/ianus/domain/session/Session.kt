package dev.tapeline.ianus.domain.session

import dev.tapeline.ianus.domain.user.UserId
import java.time.Instant
import java.time.Duration

data class Session(
    val id: SessionId,
    val token: SessionToken,
    val userId: UserId,
    val origin: SessionOrigin,
    val createdAt: Instant,
    val ttl: Duration,
) {
    val expiresAt get() = createdAt + ttl
}
