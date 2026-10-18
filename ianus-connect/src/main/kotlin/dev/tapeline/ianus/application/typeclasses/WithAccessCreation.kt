package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.interfaces.algorithms.SecureAccessKeysEngine
import dev.tapeline.ianus.application.interfaces.persistence.AccessGrantRepository
import dev.tapeline.ianus.common.newId
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.grant.AccessGrant
import dev.tapeline.ianus.domain.grant.AccessId
import dev.tapeline.ianus.domain.grant.AccessRefreshToken
import dev.tapeline.ianus.domain.grant.AccessToken
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.domain.user.UserId
import java.time.Duration
import java.time.Instant

interface WithAccessCreation {

    val accessGrantRepository: AccessGrantRepository
    val secureKeyGenerator: SecureAccessKeysEngine

}

fun WithAccessCreation.createAccessGrant(
    appId: AppId,
    scopes: Set<Scope>,
    userId: UserId,
    issuedAt: Instant,
    ttl: Duration,
) =
    AccessGrant(
        id = newId(::AccessId),
        token = secureKeyGenerator.generateAccessKey().let(::AccessToken),
        appId = appId,
        scopes = scopes,
        userId = userId,
        issuedAt = issuedAt,
        ttl = ttl,
        refreshToken = secureKeyGenerator.generateRefreshKey().let(::AccessRefreshToken)
    )
