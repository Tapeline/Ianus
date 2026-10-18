package dev.tapeline.ianus.domain.authcode

import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.domain.user.UserId
import java.time.Instant

data class AuthorisationCodeClaims(
    val appId: AppId,
    val scopes: Set<Scope>,
    val includeUserInfo: Boolean,
    val userId: UserId,
    val issuedAt: Instant,
    val expiresAt: Instant,
)
