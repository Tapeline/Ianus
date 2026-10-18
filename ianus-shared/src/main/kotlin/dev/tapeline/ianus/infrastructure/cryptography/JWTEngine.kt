package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.AESDecrypter
import com.nimbusds.jose.crypto.AESEncrypter
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import java.text.ParseException

class JWTEngine<ClaimsT>(
    private val serialiser: ClaimsSerialiser<ClaimsT>,
    private val signingKey: String,
    private val encryptionKey: String,
) {
    private val encryptionKeyByteArray = encryptionKey.toByteArray()

    fun createEncryptedToken(claims: ClaimsT): String {
        val claims = serialiser.serialiseClaims(claims)

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claims)
        signedJWT.sign(MACSigner(signingKey))

        val jwe = JWEObject(JWEHeader(JWEAlgorithm.A256GCMKW, EncryptionMethod.A256GCM), Payload(signedJWT))
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

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claims)
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
