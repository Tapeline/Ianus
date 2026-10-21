package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.authcodeflow.BeginOauthAuthorisation
import dev.tapeline.ianus.application.usecases.authcodeflow.BeginOauthAuthorisationDTO
import dev.tapeline.ianus.application.usecases.authcodeflow.GetOauthToken
import dev.tapeline.ianus.application.usecases.authcodeflow.GetOauthTokenDTO
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.authcode.AuthorisationCode
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.presentation.openapi.api.Oauth20Api
import dev.tapeline.ianus.presentation.openapi.model.OauthAuthorizeRequestResponseType
import dev.tapeline.ianus.presentation.openapi.model.OauthTokenRequestGrantType
import dev.tapeline.ianus.presentation.openapi.model.OauthTokenResponse
import dev.tapeline.ianus.presentation.openapi.model.ResponseType
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.validation.constraints.NotNull

class OauthController(
    private val beginOauthAuthroisationProvider: ObjectProvider<BeginOauthAuthorisation>,
    private val getOauthTokenProvider: ObjectProvider<GetOauthToken>
) : Oauth20Api {

    override fun getApiV1Oauth2Authorize(
        responseType: ResponseType,
        clientId: String,
        redirectUri: String,
        scope: String?,
        state: String?,
        nonce: String?
    ) = runAuthorize(clientId, redirectUri, scope, state)

    override fun postApiV1Oauth2Authorize(
        responseType: OauthAuthorizeRequestResponseType,
        clientId: String,
        redirectUri: String,
        scope: String?,
        state: String?,
        nonce: String?
    ) = runAuthorize(clientId, redirectUri, scope, state)

    private fun runAuthorize(
        clientId: String,
        redirectUri: String,
        scope: String?,
        state: String?,
    ): ResponseEntity<Void> {
        val scopes = scope?.split(" ")?.map(::Scope)?.toSet() ?: emptySet()
        val result = beginOauthAuthroisationProvider.getObject()(BeginOauthAuthorisationDTO(
            appId = AppId(clientId),
            scopes = scopes,
            includeUserInfo = scopes.any { it.isOIDC },
            redirectUri = URI.create(redirectUri),
            state = state
        ))
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(UriComponentsBuilder.fromUriString("/oauth/consent")
                .queryParam("appName", result.appName.value)
                .queryParam("scopes", result.scopes.joinToString(" "))
                .queryParam("intent", result.intentToken.value)
                .build()
                .toUri())
            .build()
    }

    override fun postApiV1Oauth2Token(
        grantType: @NotNull OauthTokenRequestGrantType?,
        code: String,
        redirectUri: String?,
        refreshToken: String?,
        clientId: String?,
        clientSecret: String?
    ): ResponseEntity<OauthTokenResponse> {
        val result = getOauthTokenProvider.getObject()(GetOauthTokenDTO(AuthorisationCode(code)))
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                OauthTokenResponse(
                    result.token.value,
                    "access",
                    result.ttl.seconds.toInt(),
                    result.refreshToken.value,
                    null,
                    result.scopes.joinToString(" ")
                )
            )
    }

}