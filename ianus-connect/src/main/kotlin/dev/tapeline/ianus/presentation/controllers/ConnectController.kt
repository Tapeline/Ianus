package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.authcodeflow.BeginOauthAuthorisation
import dev.tapeline.ianus.application.usecases.authcodeflow.BeginOauthAuthorisationDTO
import dev.tapeline.ianus.application.usecases.authcodeflow.ConsentAuthorisation
import dev.tapeline.ianus.application.usecases.authcodeflow.GetOauthToken
import dev.tapeline.ianus.application.usecases.authcodeflow.GetOauthTokenDTO
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.authcode.AuthorisationCode
import dev.tapeline.ianus.domain.intent.IntentToken
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.presentation.openapi.api.AuthoriseConsentApi
import dev.tapeline.ianus.presentation.openapi.api.Oauth20Api
import dev.tapeline.ianus.presentation.openapi.model.AuthoriseRequest
import dev.tapeline.ianus.presentation.openapi.model.OauthAuthorizeRequestResponseType
import dev.tapeline.ianus.presentation.openapi.model.OauthTokenRequestGrantType
import dev.tapeline.ianus.presentation.openapi.model.OauthTokenResponse
import dev.tapeline.ianus.presentation.openapi.model.ResponseType
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.NotNull

class ConnectController(
    private val consentAuthorisationActionProvider: ObjectProvider<ConsentAuthorisation>,
) : AuthoriseConsentApi {

    override fun postApiV1ConnectAuthorize(body: @Valid @NotNull AuthoriseRequest): ResponseEntity<Void>? {
        val result = consentAuthorisationActionProvider.getObject()(IntentToken(body.intent))
        return ResponseEntity<Void>.status(HttpStatus.FOUND)
            .location(UriComponentsBuilder.fromUri(result.redirectUri)
                .queryParam("code", result.authCode.value)
                .build()
                .toUri())
            .build()
    }

}