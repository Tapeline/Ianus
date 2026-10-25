package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.IntentEngine
import dev.tapeline.ianus.configuration.IntentConfiguration
import dev.tapeline.ianus.domain.intent.Intent
import dev.tapeline.ianus.domain.intent.IntentToken
import org.springframework.stereotype.Component

@Component
class JwtIntentEngine(
    intentConfig: IntentConfiguration
) : IntentEngine {

    private val jwtEngine = JWTEngine(
        JwtIntentClaimsSerialiser(),
        intentConfig.signingKey,
        intentConfig.encryptionKey,
    )

    override fun createIntentToken(intent: Intent) =
        jwtEngine.createOpenToken(intent).let(::IntentToken)

    override fun extractIntent(intentToken: IntentToken) =
        jwtEngine.verifyAndExtractOpenClaims(intentToken.value)

}
