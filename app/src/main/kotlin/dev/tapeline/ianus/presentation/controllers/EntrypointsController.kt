package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.login.BeginLoginAction
import dev.tapeline.ianus.application.usecases.login.LoginDTO
import dev.tapeline.ianus.application.usecases.login.LoginResult
import dev.tapeline.ianus.application.usecases.register.RegisterAction
import dev.tapeline.ianus.application.usecases.register.RegisterDTO
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.user.Username
import dev.tapeline.ianus.presentation.SECOND_FACTOR_VERIFICATION_COOKIE
import dev.tapeline.ianus.presentation.SESSION_TOKEN_COOKIE
import dev.tapeline.ianus.presentation.SET_COOKIE
import dev.tapeline.ianus.presentation.createSecureCookie
import dev.tapeline.ianus.presentation.headers
import dev.tapeline.ianus.presentation.openapi.api.EntrypointsApi
import dev.tapeline.ianus.presentation.openapi.model.LoginRequest
import dev.tapeline.ianus.presentation.openapi.model.LoginRequires2Faresponse
import dev.tapeline.ianus.presentation.openapi.model.LoginResponse
import dev.tapeline.ianus.presentation.openapi.model.RegisterRequest
import dev.tapeline.ianus.presentation.openapi.model.RegisterResponse
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class EntrypointsController(
    private val loginActionProvider: ObjectProvider<BeginLoginAction>,
    private val registerActionProvider: ObjectProvider<RegisterAction>,
    private val tfaConfig: TwoFactorConfiguration,
) : EntrypointsApi {

    override fun postApiV1Login(
        userAgent: @NotNull String,
        body: @Valid @NotNull LoginRequest
    ) =
        when (
            val result = loginActionProvider.getObject()(
                LoginDTO(Username(body.username), body.password, SessionOrigin(userAgent))
            )
        ) {
            is LoginResult.Successful -> ResponseEntity(
                LoginResponse(result.userId.value, result.sessionId.value),
                headers(
                    SET_COOKIE to createSecureCookie(
                        SESSION_TOKEN_COOKIE,
                        result.sessionToken.value,
                        result.sessionTtl
                    )
                ),
                HttpStatus.CREATED
            )

            is LoginResult.Requires2FA -> ResponseEntity(
                LoginRequires2Faresponse(result.userId.value),
                headers(
                    SET_COOKIE to createSecureCookie(
                        SECOND_FACTOR_VERIFICATION_COOKIE,
                        result.twoFactorKey.value,
                        tfaConfig.verificationTtl
                    )
                ),
                HttpStatus.OK
            )
        }


    override fun postApiV1Register(
        userAgent: @NotNull String,
        body: @Valid @NotNull RegisterRequest
    ) =
        registerActionProvider.getObject()(
            RegisterDTO(Username(body.username), body.password, SessionOrigin(userAgent))
        )
            .run {
                ResponseEntity(
                    RegisterResponse(userId.value, sessionId.value),
                    headers(
                        SET_COOKIE to createSecureCookie(
                            SESSION_TOKEN_COOKIE,
                            sessionToken.value,
                            sessionTtl
                        )
                    ),
                    HttpStatus.CREATED,
                )
            }

}
