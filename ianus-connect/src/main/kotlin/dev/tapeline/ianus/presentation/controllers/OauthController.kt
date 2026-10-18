package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.authcodeflow.BeginOauthAuthorisation
import dev.tapeline.ianus.presentation.openapi.api.Oauth20Api
import dev.tapeline.ianus.presentation.openapi.model.OauthAuthorizeRequestResponseType
import dev.tapeline.ianus.presentation.openapi.model.OauthTokenRequestGrantType
import dev.tapeline.ianus.presentation.openapi.model.OauthTokenResponse
import dev.tapeline.ianus.presentation.openapi.model.ResponseType
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.ResponseEntity
import javax.validation.constraints.NotNull

class OauthController(
    private val beginOauthAuthroisationProvider: ObjectProvider<BeginOauthAuthorisation>
) : Oauth20Api {
    override fun getApiV1Oauth2Authorize(
        responseType: ResponseType,
        clientId: String,
        redirectUri: String,
        scope: String?,
        state: String?,
        nonce: String?
    ): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun postApiV1Oauth2Authorize(
        responseType: OauthAuthorizeRequestResponseType,
        clientId: String,
        redirectUri: String,
        scope: String?,
        state: String?,
        nonce: String?
    ): ResponseEntity<Void> {
        TODO("Not yet implemented")
    }

    override fun postApiV1Oauth2Token(
        grantType: @NotNull OauthTokenRequestGrantType?,
        code: String?,
        redirectUri: String?,
        refreshToken: String?,
        clientId: String?,
        clientSecret: String?
    ): ResponseEntity<OauthTokenResponse> {
        TODO("Not yet implemented")
    }

}