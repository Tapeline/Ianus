package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.authcodeflow.ConsentAuthorisationAction
import dev.tapeline.ianus.domain.intent.IntentToken
import dev.tapeline.ianus.presentation.openapi.api.AuthoriseConsentApi
import dev.tapeline.ianus.presentation.openapi.model.AuthoriseRequest
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class ConnectController(
    private val consentAuthorisationActionActionProvider: ObjectProvider<ConsentAuthorisationAction>,
) : AuthoriseConsentApi {

    override fun postApiV1ConnectAuthorize(body: @Valid @NotNull AuthoriseRequest): ResponseEntity<Void>? {
        val result = consentAuthorisationActionActionProvider.getObject()(IntentToken(body.intent))
        return ResponseEntity<Void>.status(HttpStatus.FOUND)
            .location(UriComponentsBuilder.fromUri(result.redirectUri)
                .queryParam("code", result.authCode.value)
                .build()
                .toUri())
            .build()
    }

}