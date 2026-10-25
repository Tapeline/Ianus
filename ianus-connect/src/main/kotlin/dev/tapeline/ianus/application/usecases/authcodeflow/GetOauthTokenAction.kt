package dev.tapeline.ianus.application.usecases.authcodeflow

import dev.tapeline.ianus.application.exceptions.ExpiredAuthorisationCodeException
import dev.tapeline.ianus.application.interfaces.algorithms.AuthorisationCodeEngine
import dev.tapeline.ianus.application.interfaces.algorithms.PasswordHashEngine
import dev.tapeline.ianus.application.interfaces.algorithms.SecureAccessKeysEngine
import dev.tapeline.ianus.application.interfaces.environment.AppAuthenticationEnvironment
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.AccessGrantRepository
import dev.tapeline.ianus.application.interfaces.persistence.AppRepository
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.typeclasses.WithAccessCreation
import dev.tapeline.ianus.application.typeclasses.WithAppAuthenticationEnvironment
import dev.tapeline.ianus.application.typeclasses.createAccessGrant
import dev.tapeline.ianus.application.typeclasses.withSignedApp
import dev.tapeline.ianus.configuration.AccessTokenConfiguration
import dev.tapeline.ianus.domain.authcode.AuthorisationCode
import dev.tapeline.ianus.domain.grant.AccessGrant
import org.springframework.stereotype.Component

data class GetOauthTokenDTO(
    val code: AuthorisationCode
)

data class GetOauthTokenResult(
    val grant: AccessGrant
)

@Component
class GetOauthTokenAction(
    override val apps: AppRepository,
    override val passwordHashEngine: PasswordHashEngine,
    private val authorisationCodeEngine: AuthorisationCodeEngine,
    private val clock: Clock,
    private val accessTokenConfig: AccessTokenConfiguration,
    private val tx: Transaction,
    override val accessGrants: AccessGrantRepository,
    override val secureKeyGenerator: SecureAccessKeysEngine,
) : WithAppAuthenticationEnvironment, WithAccessCreation {

    override lateinit var appAuthenticationEnvironment: AppAuthenticationEnvironment

    operator fun invoke(dto: GetOauthTokenDTO) =
        withSignedApp { app ->
            val claims = authorisationCodeEngine.extractClaims(dto.code)
            val now = clock.now()
            if (now > claims.expiresAt) throw ExpiredAuthorisationCodeException()
            tx {
                createAccessGrant(
                    claims.appId, claims.scopes, claims.userId,
                    now, accessTokenConfig.accessTokenTtl
                )
            }
        }

}
