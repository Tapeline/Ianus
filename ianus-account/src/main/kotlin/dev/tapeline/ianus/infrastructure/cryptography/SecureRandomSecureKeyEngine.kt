package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.SecureKeyEngine
import dev.tapeline.ianus.configuration.SessionConfiguration
import org.springframework.stereotype.Component
import kotlin.io.encoding.ExperimentalEncodingApi

@Component
class SecureRandomSecureKeyEngine(
    private val sessionConfig: SessionConfiguration
) : SecureKeyEngine {
    @OptIn(ExperimentalEncodingApi::class)
    override fun generateSessionSecret() =
        secureRandomBase64OfLength(sessionConfig.sessionIdLengthBytes)
}
