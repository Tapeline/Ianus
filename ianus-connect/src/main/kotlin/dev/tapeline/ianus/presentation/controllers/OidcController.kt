package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.configuration.TOKEN_SIGNING_ALGORITHM_STR
import dev.tapeline.ianus.configuration.TokenIssuingConfiguration
import dev.tapeline.ianus.presentation.openapi.api.OpenIdConnect10Api
import dev.tapeline.ianus.presentation.openapi.model.Jwk
import dev.tapeline.ianus.presentation.openapi.model.Jwks
import dev.tapeline.ianus.presentation.openapi.model.OauthUserInfoResponse
import dev.tapeline.ianus.presentation.openapi.model.OidConfig
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class OidcController(
    private val tokenIssuingConfig: TokenIssuingConfiguration
) : OpenIdConnect10Api {
    override fun getApiV1Oauth2Userinfo(): ResponseEntity<OauthUserInfoResponse>? {
        TODO("Not yet implemented")
    }

    override fun getApiV1Oauth2Jwks(): ResponseEntity<Jwks> =
        ResponseEntity.status(200).body(
            Jwks(
                arrayOf(
                    Jwk(
                        "RSA",
                        "token-0",
                        "sig",
                        TOKEN_SIGNING_ALGORITHM_STR,
                        null,
                        null
                    )
                )
            )
        )

    override fun getWellKnownOpenidConfiguration(): ResponseEntity<OidConfig> =
        ResponseEntity.status(200).body(
            OidConfig(
                tokenIssuingConfig.issuer,
                "/api/v1/oauth2/authorize",
                "/api/v1/oauth2/token",
                null,
                "/api/v1/oauth2/jwks",
                arrayOf("openid"),
                arrayOf("code", "id_token", "code id_token", "id_token code"),
                arrayOf("public"),
                arrayOf(TOKEN_SIGNING_ALGORITHM_STR)
            )
        )

}
