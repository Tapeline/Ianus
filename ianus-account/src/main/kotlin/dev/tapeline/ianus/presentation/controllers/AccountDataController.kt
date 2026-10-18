package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.changeemail.BeginChangeEmailAction
import dev.tapeline.ianus.application.usecases.changeemail.BeginChangeEmailDTO
import dev.tapeline.ianus.application.usecases.changeemail.ConfirmChangeEmailAction
import dev.tapeline.ianus.application.usecases.changeemail.ConfirmChangeEmailDTO
import dev.tapeline.ianus.application.usecases.getmy.GetMyAccountAction
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig
import dev.tapeline.ianus.domain.user.Email
import dev.tapeline.ianus.presentation.SECOND_FACTOR_VERIFICATION_COOKIE
import dev.tapeline.ianus.presentation.SET_COOKIE
import dev.tapeline.ianus.presentation.createSecureCookie
import dev.tapeline.ianus.presentation.headers
import dev.tapeline.ianus.presentation.openapi.api.AccountDataApi
import dev.tapeline.ianus.presentation.openapi.model.*
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class AccountDataController(
    private val getMyAccountActionProvider: ObjectProvider<GetMyAccountAction>,
    private val beginChangeEmailActionProvider: ObjectProvider<BeginChangeEmailAction>,
    private val confirmChangeEmailActionProvider: ObjectProvider<ConfirmChangeEmailAction>,
    private val tfaConfiguration: TwoFactorConfiguration
) : AccountDataApi {

    override fun getApiV1My() =
        getMyAccountActionProvider.getObject()()
            .run { MyAccountResponse(username.value, email?.value, twoFactor != TwoFactorConfig.DISABLED) }
            .to200OK()

    override fun postApiV1MyEmailChange(
        userAgent: @NotNull String,
        body: @Valid @NotNull BeginEmailChangeRequest
    ) =
        beginChangeEmailActionProvider.getObject()(
            BeginChangeEmailDTO(
                Email(body.email),
                SessionOrigin(userAgent)
            )
        )
            .run {
                ResponseEntity(
                    BeginEmailChangeResponse(body.email),
                    headers(
                        SET_COOKIE to createSecureCookie(
                            SECOND_FACTOR_VERIFICATION_COOKIE,
                            key.value,
                            tfaConfiguration.verificationTtl
                        )
                    ),
                    HttpStatus.CREATED
                )
            }


    override fun putApiV1MyEmailConfirm(body: @Valid ConfirmEmailChangeRequest) =
        confirmChangeEmailActionProvider.getObject()(
            ConfirmChangeEmailDTO(TwoFactorCode(body.code), Email(body.email))
        )
            .run { ConfirmEmailChangeResponse(email.value) }
            .to200OK()

}
