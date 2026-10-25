package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.AuthorisationCodeEngine
import dev.tapeline.ianus.configuration.AuthorisationCodeConfiguration
import dev.tapeline.ianus.domain.authcode.AuthorisationCode
import dev.tapeline.ianus.domain.authcode.AuthorisationCodeClaims
import org.springframework.stereotype.Component

@Component
class JwtAuthorisatonCodeEngine(
    authCodeConfig: AuthorisationCodeConfiguration
) : AuthorisationCodeEngine {

    private val jwtEngine = JWTEngine(
        JwtAuthorisationCodeClaimsSerialiser(),
        authCodeConfig.signingKey,
        authCodeConfig.encryptionKey,
    )

    override fun createAuthorisationCode(claims: AuthorisationCodeClaims) =
        jwtEngine.createOpenToken(claims).let(::AuthorisationCode)

    override fun extractClaims(code: AuthorisationCode) =
        jwtEngine.verifyAndExtractOpenClaims(code.value)

}
