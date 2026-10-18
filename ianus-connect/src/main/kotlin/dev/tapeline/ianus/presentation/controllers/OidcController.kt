package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.presentation.openapi.api.OpenIdConnect10Api
import dev.tapeline.ianus.presentation.openapi.model.Jwks
import dev.tapeline.ianus.presentation.openapi.model.OauthUserInfoResponse
import dev.tapeline.ianus.presentation.openapi.model.OidConfig
import org.springframework.http.ResponseEntity

class OidcController : OpenIdConnect10Api {
    override fun getApiV1Oauth2Userinfo(): ResponseEntity<OauthUserInfoResponse>? {
        TODO("Not yet implemented")
    }

    override fun getApiV1Oauth2Jwks(): ResponseEntity<Jwks>? {
        TODO("Not yet implemented")
    }

    override fun getWellKnownOpenidConfiguration(): ResponseEntity<OidConfig>? {
        TODO("Not yet implemented")
    }
}