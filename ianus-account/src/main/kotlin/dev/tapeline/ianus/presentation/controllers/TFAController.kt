package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.login.ConfirmTwoFactorLoginAction
import dev.tapeline.ianus.application.usecases.login.TwoFactorCodeDTO
import dev.tapeline.ianus.application.usecases.toggle2fa.Toggle2FAAction
import dev.tapeline.ianus.application.usecases.toggle2fa.Toggle2FADTO
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.presentation.SECOND_FACTOR_PERSISTENCE_COOKIE
import dev.tapeline.ianus.presentation.SESSION_TOKEN_COOKIE
import dev.tapeline.ianus.presentation.SET_COOKIE
import dev.tapeline.ianus.presentation.createSecureCookie
import dev.tapeline.ianus.presentation.multiheaders
import dev.tapeline.ianus.presentation.openapi.api.FaApi
import dev.tapeline.ianus.presentation.openapi.model.LoginResponse
import dev.tapeline.ianus.presentation.openapi.model.TfaconfirmationRequest
import dev.tapeline.ianus.presentation.openapi.model.Toggle2Farequest
import dev.tapeline.ianus.presentation.openapi.model.Toggle2Faresponse
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class TFAController(
    private val confirmTwoFactorLoginActionProvider: ObjectProvider<ConfirmTwoFactorLoginAction>,
    private val toggle2FAActionProvider: ObjectProvider<Toggle2FAAction>,
    private val tfaConfiguration: TwoFactorConfiguration
) : FaApi {

    override fun postApiV1FaConfirm(body: @Valid @NotNull TfaconfirmationRequest) =
        confirmTwoFactorLoginActionProvider.getObject()(TwoFactorCodeDTO(TwoFactorCode(body.code)))
            .run {
                ResponseEntity(
                    LoginResponse(userId.value, sessionId.value),
                    multiheaders(
                        SET_COOKIE to createSecureCookie(
                            SESSION_TOKEN_COOKIE,
                            sessionToken.value,
                            sessionTtl
                        ),
                        SET_COOKIE to createSecureCookie(
                            SECOND_FACTOR_PERSISTENCE_COOKIE,
                            persistent2FA.value,
                            tfaConfiguration.persistentTtl
                        )
                    ),
                    HttpStatus.CREATED
                )
            }

    override fun putApiV1FaToggle(body: @Valid @NotNull Toggle2Farequest) =
        toggle2FAActionProvider.getObject()(Toggle2FADTO(body.faEnabled))
            .run { Toggle2Faresponse(enabled) }
            .to200OK()

}