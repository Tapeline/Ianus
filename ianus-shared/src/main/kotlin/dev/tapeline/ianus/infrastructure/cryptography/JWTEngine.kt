package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.AESDecrypter
import com.nimbusds.jose.crypto.AESEncrypter
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import dev.tapeline.ianus.configuration.ENCRYPTION_MODE
import dev.tapeline.ianus.configuration.ENCRYPTION_ALGORITHM
import dev.tapeline.ianus.configuration.TOKEN_SIGNING_ALGORITHM
import java.text.ParseException

class JWTEngine<ClaimsT>(
    private val serialiser: ClaimsSerialiser<ClaimsT>,
    private val signingKey: String,
    private val encryptionKey: String,
    private val signingAlgorithm: JWSAlgorithm = TOKEN_SIGNING_ALGORITHM,
    private val encryptionAlgorithm: JWEAlgorithm = ENCRYPTION_ALGORITHM,
    private val encryptionMethod: EncryptionMethod = ENCRYPTION_MODE,
) {
    private val encryptionKeyByteArray = encryptionKey.toByteArray()

    fun createEncryptedToken(claims: ClaimsT): String {
        val claims = serialiser.serialiseClaims(claims)

        val signedJWT = SignedJWT(JWSHeader(signingAlgorithm), claims)
        signedJWT.sign(MACSigner(signingKey))

        val jwe = JWEObject(JWEHeader(encryptionAlgorithm, encryptionMethod), Payload(signedJWT))
        jwe.encrypt(AESEncrypter(encryptionKeyByteArray))

        return jwe.serialize()
    }

    fun verifyAndExtractEncryptedClaims(token: String): ClaimsT {
        try {
            val jweObject = JWEObject.parse(token)
            jweObject.decrypt(AESDecrypter(encryptionKeyByteArray))

            val signedJWT = jweObject.payload.toSignedJWT()
                ?: throw CannotDeserialiseTokenException()

            if (!signedJWT.verify(MACVerifier(encryptionKey)))
                throw CannotDeserialiseTokenException()

            return serialiser.deserialiseClaims(signedJWT.jwtClaimsSet)
        } catch (e: Exception) {
            when (e) {
                is ParseException, is JOSEException -> throw CannotDeserialiseTokenException()
                else -> throw e
            }
        }
    }

    fun createOpenToken(claims: ClaimsT): String {
        val claims = serialiser.serialiseClaims(claims)

        val signedJWT = SignedJWT(JWSHeader(signingAlgorithm), claims)
        signedJWT.sign(MACSigner(signingKey))

        return signedJWT.serialize()
    }

    fun verifyAndExtractOpenClaims(token: String): ClaimsT {
        try {
            val jwt = SignedJWT.parse(token)

            if (!jwt.verify(MACVerifier(signingKey)))
                throw CannotDeserialiseTokenException()

            return serialiser.deserialiseClaims(jwt.jwtClaimsSet)
        } catch (e: Exception) {
            when (e) {
                is ParseException, is JOSEException -> throw CannotDeserialiseTokenException()
                else -> throw e
            }
        }
    }

}
