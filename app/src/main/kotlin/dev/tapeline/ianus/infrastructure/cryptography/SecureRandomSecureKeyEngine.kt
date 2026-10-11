package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.SecureKeyEngine
import dev.tapeline.ianus.common.newUuid
import dev.tapeline.ianus.configuration.SessionConfiguration
import org.springframework.stereotype.Component
import java.security.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Component
class SecureRandomSecureKeyEngine(
    private val sessionConfig: SessionConfiguration
) : SecureKeyEngine {
    @OptIn(ExperimentalEncodingApi::class)
    override fun generateSessionSecret() =
        ByteArray(sessionConfig.sessionIdLengthBytes)
            .apply(SecureRandom()::nextBytes)
            .run(Base64::encode)
            .let { newUuid().toString() + "-" + it }
}