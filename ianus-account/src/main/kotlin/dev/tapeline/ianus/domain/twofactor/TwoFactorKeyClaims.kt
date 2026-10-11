package dev.tapeline.ianus.domain.twofactor

import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.user.UserId
import java.time.Instant
import java.time.Duration


data class TwoFactorKeyClaims(
    val userId: UserId,
    val loginOrigin: SessionOrigin,
    val issuedAt: Instant,
    val ttl: Duration
) {
    val expiresAt get() = issuedAt + ttl
}
