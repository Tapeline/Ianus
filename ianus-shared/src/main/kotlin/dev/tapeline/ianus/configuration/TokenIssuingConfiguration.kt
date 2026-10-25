package dev.tapeline.ianus.configuration

import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWSAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties

val TOKEN_SIGNING_ALGORITHM: JWSAlgorithm = JWSAlgorithm.RS256
const val TOKEN_SIGNING_ALGORITHM_STR = "rs256"

val ENCRYPTION_ALGORITHM: JWEAlgorithm = JWEAlgorithm.A256GCMKW
val ENCRYPTION_MODE: EncryptionMethod = EncryptionMethod.A256GCM

@ConfigurationProperties(prefix = "issuer")
data class TokenIssuingConfiguration(
    val issuer: String,
)
