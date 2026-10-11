package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.AESDecrypter
import com.nimbusds.jose.crypto.AESEncrypter
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dev.tapeline.ianus.application.exceptions.TwoFactorVerificationException
import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.domain.twofactor.TwoFactorKeyClaims
import dev.tapeline.ianus.domain.user.UserId
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.Duration
import java.util.Date

private const val USER_ID_CLAIM = "u"
private const val ORIGIN_CLAIM = "o"
private const val CODE_CLAIM = "c"

@Component
class JweTwoFactorVerificationEngine(
    private val clock: Clock,
    private val twoFactorConfig: TwoFactorConfiguration
) : TwoFactorVerificationEngine {
    override fun createTwoFactorPair(claims: TwoFactorKeyClaims): Pair<TwoFactorCode, TwoFactorKey> {
        val code = generateRandomCode()
        val claims = JWTClaimsSet.Builder()
            .issueTime(Date.from(claims.issuedAt))
            .expirationTime(Date.from(claims.expiresAt))
            .claim(USER_ID_CLAIM, claims.userId.value)
            .claim(ORIGIN_CLAIM, claims.loginOrigin.value)
            .claim(CODE_CLAIM, code)  // TODO: replace with hash
            .build()

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claims)
        signedJWT.sign(MACSigner(twoFactorConfig.signingKey))

        val jwe = JWEObject(JWEHeader(JWEAlgorithm.A256GCMKW, EncryptionMethod.A256GCM), Payload(signedJWT))
        jwe.encrypt(AESEncrypter(twoFactorConfig.encryptionKey.toByteArray()))

        return Pair(TwoFactorCode(code), TwoFactorKey(jwe.serialize()))
    }

    override fun twoFactorCodeValid(
        code: TwoFactorCode,
        key: TwoFactorKey
    ): Boolean {
        try {
            val jweObject = JWEObject.parse(key.value)
            jweObject.decrypt(AESDecrypter(twoFactorConfig.encryptionKey.toByteArray()))

            val signedJWT = jweObject.payload.toSignedJWT()
                ?: throw TwoFactorVerificationException()

            if (!signedJWT.verify(MACVerifier(twoFactorConfig.encryptionKey)))
                throw TwoFactorVerificationException()

            return signedJWT.jwtClaimsSet.getStringClaim(CODE_CLAIM) == code.value
        } catch (_: Exception) {
            return false
        }
    }

    override fun extractClaims(key: TwoFactorKey): TwoFactorKeyClaims {
        val jweObject = JWEObject.parse(key.value)
        jweObject.decrypt(AESDecrypter(twoFactorConfig.encryptionKey.toByteArray()))

        val signedJWT = jweObject.payload.toSignedJWT()
            ?: throw TwoFactorVerificationException()

        if (!signedJWT.verify(MACVerifier(twoFactorConfig.encryptionKey)))
            throw TwoFactorVerificationException()

        return with (signedJWT.jwtClaimsSet) {
            TwoFactorKeyClaims(
                userId = UserId(getStringClaim(USER_ID_CLAIM)),
                loginOrigin = SessionOrigin(getStringClaim(ORIGIN_CLAIM)),
                issuedAt = issueTime.toInstant(),
                ttl = Duration.ofMillis(expirationTime.time - issueTime.time)
            )
        }
    }

}

private fun generateRandomCode() =
    SecureRandom().nextInt(0, 1000000).toString().padStart(6, '0')
