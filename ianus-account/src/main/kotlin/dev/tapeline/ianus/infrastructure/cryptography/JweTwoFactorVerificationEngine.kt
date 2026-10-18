package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.exceptions.TwoFactorVerificationException
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.domain.twofactor.TwoFactorKeyClaims
import org.springframework.stereotype.Component

@Component
class JweTwoFactorVerificationEngine(
    twoFactorConfig: TwoFactorConfiguration,
) : TwoFactorVerificationEngine {

    private val jwtEngine = JWTEngine(
        JweTwoFactorKeyClaimsSerialiser(),
        twoFactorConfig.signingKey,
        twoFactorConfig.encryptionKey,
    )

    override fun createTwoFactorPair(claims: TwoFactorKeyClaims): Pair<TwoFactorCode, TwoFactorKey> =
        Pair(claims.code, TwoFactorKey(jwtEngine.createEncryptedToken(claims)))

    override fun extractClaims(key: TwoFactorKey) =
        try {
            jwtEngine.verifyAndExtractEncryptedClaims(key.value)
        } catch (_: CannotDeserialiseTokenException) {
            throw TwoFactorVerificationException()
        }

}
