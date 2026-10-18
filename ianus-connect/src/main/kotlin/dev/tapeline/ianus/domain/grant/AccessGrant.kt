package dev.tapeline.ianus.domain.grant

import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.domain.user.UserId
import java.time.Duration
import java.time.Instant

data class AccessGrant(
    val id: AccessId,
    val token: AccessToken,
    val appId: AppId,
    val scopes: Set<Scope>,
    val userId: UserId,
    val issuedAt: Instant,
    val ttl: Duration,
    val refreshToken: AccessRefreshToken,
    val revoked: Boolean = false
) {
    val expiresAt get() = issuedAt + ttl
}
