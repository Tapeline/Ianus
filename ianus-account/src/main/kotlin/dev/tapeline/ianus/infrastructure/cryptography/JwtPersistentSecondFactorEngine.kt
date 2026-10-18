package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.exceptions.SecondFactorNotPresentException
import dev.tapeline.ianus.application.interfaces.algorithms.PersistentSecondFactorEngine
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.twofactor.SecondFactor
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import org.springframework.stereotype.Component

@Component
class JwtPersistentSecondFactorEngine(
    twoFactorConfig: TwoFactorConfiguration
) : PersistentSecondFactorEngine {

    private val jwtEngine = JWTEngine(
        JwtPersistentSecondFactorClaimsSerialiser(),
        twoFactorConfig.signingKey,
        twoFactorConfig.encryptionKey,
    )

    override fun issueSecondFactorToken(secondFactor: SecondFactor) =
        SecondFactorToken(jwtEngine.createOpenToken(secondFactor))

    override fun validateAndExtractSecondFactor(token: SecondFactorToken) =
        try {
            jwtEngine.verifyAndExtractOpenClaims(token.value)
        } catch (_: CannotDeserialiseTokenException) {
            throw SecondFactorNotPresentException()
        }

}
