package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dev.tapeline.ianus.application.exceptions.SecondFactorNotPresentException
import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.algorithms.PersistentSecondFactorEngine
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.twofactor.SecondFactor
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import dev.tapeline.ianus.domain.user.UserId
import org.springframework.stereotype.Component
import java.text.ParseException
import java.time.Duration
import java.util.*

private const val USER_ID_CLAIM = "u"

@Component
class JwtPersistentSecondFactorEngine(
    private val clock: Clock,
    private val twoFactorConfig: TwoFactorConfiguration
) : PersistentSecondFactorEngine {
    override fun issueSecondFactorToken(secondFactor: SecondFactor): SecondFactorToken {
        val claims = JWTClaimsSet.Builder()
            .issueTime(Date.from(secondFactor.issuedAt))
            .expirationTime(Date.from(secondFactor.expiresAt))
            .claim(USER_ID_CLAIM, secondFactor.userId)
            .build()
        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claims)
        signedJWT.sign(MACSigner(twoFactorConfig.signingKey))
        return SecondFactorToken(signedJWT.serialize())
    }

    override fun validateAndExtractSecondFactor(token: SecondFactorToken): SecondFactor {
        try {
            val jwt = SignedJWT.parse(token.value)

            if (!jwt.verify(MACVerifier(twoFactorConfig.signingKey)))
                throw SecondFactorNotPresentException()

            return with (jwt.jwtClaimsSet) {
                SecondFactor(
                    userId = UserId(getStringClaim(USER_ID_CLAIM)),
                    issuedAt = issueTime.toInstant(),
                    ttl = Duration.ofMillis(expirationTime.time - issueTime.time)
                )
            }
        } catch (e: Exception) {
            when (e) {
                is ParseException, is JOSEException ->
                    throw SecondFactorNotPresentException()
                else ->
                    throw e
            }
        }
    }
}