package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.SecureAccessKeysEngine
import dev.tapeline.ianus.configuration.AccessTokenConfiguration
import org.springframework.stereotype.Component
import kotlin.io.encoding.ExperimentalEncodingApi

@Component
class SecureRandomAccessKeysEngine(
    private val accessTokenConfig: AccessTokenConfiguration
) : SecureAccessKeysEngine {

    @OptIn(ExperimentalEncodingApi::class)
    override fun generateAccessKey() =
        secureRandomBase64OfLength(accessTokenConfig.accessTokenLengthBytes)

    @OptIn(ExperimentalEncodingApi::class)
    override fun generateRefreshKey() =
        secureRandomBase64OfLength(accessTokenConfig.refreshTokenLengthBytes)

}
