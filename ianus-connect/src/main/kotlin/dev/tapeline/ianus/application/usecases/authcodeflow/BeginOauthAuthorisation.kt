package dev.tapeline.ianus.application.usecases.authcodeflow

import dev.tapeline.ianus.application.exceptions.AppMisconfiguredException
import dev.tapeline.ianus.application.exceptions.AppNotFoundException
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.environment.UserAuthenticator
import dev.tapeline.ianus.application.interfaces.algorithms.IntentEngine
import dev.tapeline.ianus.application.interfaces.persistence.AppRepository
import dev.tapeline.ianus.application.typeclasses.WithUserAuthenticator
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.configuration.IntentConfiguration
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.app.AppName
import dev.tapeline.ianus.domain.intent.Intent
import dev.tapeline.ianus.domain.intent.IntentToken
import dev.tapeline.ianus.domain.scope.Scope
import java.net.URI

data class BeginOauthAuthorisationDTO(
    val appId: AppId,
    val scopes: Set<Scope>,
    val includeUserInfo: Boolean,
    val redirectUri: URI,
    val state: String?
)

data class BeginOauthAuthorisationResult(
    val intentToken: IntentToken,
    val appName: AppName,
    val scopes: Set<Scope>
)

class BeginOauthAuthorisation(
    override val userAuthenticator: UserAuthenticator,
    private val apps: AppRepository,
    private val intentEngine: IntentEngine,
    private val clock: Clock,
    private val tx: Transaction,
    private val intentConfig: IntentConfiguration,
) : WithUserAuthenticator {

    operator fun invoke(dto: BeginOauthAuthorisationDTO) = tx {
        withSignedUser { user ->
            val app = apps.getApp(dto.appId) ?: throw AppNotFoundException()
            if (app.redirectUri != dto.redirectUri || (dto.scopes - app.scopes).isNotEmpty())
                throw AppMisconfiguredException()
            val now = clock.now()
            BeginOauthAuthorisationResult(
                intentEngine.createIntentToken(Intent(
                    dto.appId,
                    dto.scopes,
                    dto.includeUserInfo,
                    dto.redirectUri,
                    dto.state,
                    now,
                    now + intentConfig.ttl
                )),
                app.name,
                app.scopes
            )
        }
    }
        // authentify user
        // generate intent

        // ask for permission
        // generate signed key with payload:
            // who the user is
            // who the app is
            // what is granted
}