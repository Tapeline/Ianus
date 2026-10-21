package dev.tapeline.ianus.domain.intent

import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.scope.Scope
import java.net.URI
import java.time.Instant

data class Intent(
    val appId: AppId,
    val scopes: Set<Scope>,
    val includeUserInfo: Boolean,
    val redirectUri: URI,
    val state: String?,
    val issuedAt: Instant,
    val expiresAt: Instant,
)
