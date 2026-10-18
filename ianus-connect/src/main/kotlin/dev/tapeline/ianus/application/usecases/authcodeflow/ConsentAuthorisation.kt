package dev.tapeline.ianus.application.usecases.authcodeflow

import dev.tapeline.ianus.application.exceptions.ExpiredIntentException
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.environment.UserAuthenticator
import dev.tapeline.ianus.application.interfaces.algorithms.AuthorisationCodeEngine
import dev.tapeline.ianus.application.interfaces.algorithms.IntentEngine
import dev.tapeline.ianus.application.typeclasses.WithUserAuthenticator
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.configuration.AuthorisationCodeConfiguration
import dev.tapeline.ianus.domain.authcode.AuthorisationCodeClaims
import dev.tapeline.ianus.domain.intent.IntentToken

class ConsentAuthorisation(
    private val tx: Transaction,
    private val intentEngine: IntentEngine,
    override val userAuthenticator: UserAuthenticator,
    private val clock: Clock,
    private val authorisationCodeEngine: AuthorisationCodeEngine,
    private val authorisationCodeConfig: AuthorisationCodeConfiguration
) : WithUserAuthenticator {

    operator fun invoke(intentToken: IntentToken) =
        intentEngine.extractIntent(intentToken).let { intent ->
            val now = clock.now()
            if (now > intent.expiresAt) throw ExpiredIntentException()
            tx {
                withSignedUser { user ->
                    authorisationCodeEngine.createAuthorisationCode(
                        AuthorisationCodeClaims(
                            appId = intent.appId,
                            scopes = intent.scopes,
                            includeUserInfo = intent.includeUserInfo,
                            userId = user.id,
                            issuedAt = now,
                            expiresAt = now + authorisationCodeConfig.ttl
                        )
                    )
                }
            }
        }

}
