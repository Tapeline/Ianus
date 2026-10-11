package dev.tapeline.ianus.presentation

import dev.tapeline.ianus.application.exceptions.EmailNotSetUpException
import dev.tapeline.ianus.application.exceptions.InsecurePasswordException
import dev.tapeline.ianus.application.exceptions.Invalid2FACodeException
import dev.tapeline.ianus.application.exceptions.InvalidCredentialsException
import dev.tapeline.ianus.application.exceptions.RegistrationDisabledException
import dev.tapeline.ianus.application.exceptions.SecondFactorNotPresentException
import dev.tapeline.ianus.application.exceptions.TwoFactorVerificationException
import dev.tapeline.ianus.application.exceptions.UnauthenticatedException
import dev.tapeline.ianus.application.exceptions.UnauthorisedException
import dev.tapeline.ianus.application.exceptions.UserAlreadyExistsException
import dev.tapeline.ianus.domain.IanusException
import dev.tapeline.ianus.presentation.openapi.model.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionMapping {

    @ExceptionHandler(IanusException::class)
    fun handleException(exc: IanusException) =
        ResponseEntity
            .status(mapStatusCode(exc))
            .body(ErrorResponse(exc.code, exc.details))

    private fun mapStatusCode(exc: IanusException) = when (exc) {
        is UnauthorisedException,
        is RegistrationDisabledException -> 403

        is UnauthenticatedException,
        is Invalid2FACodeException,
        is InvalidCredentialsException,
        is SecondFactorNotPresentException,
        is TwoFactorVerificationException -> 401

        is EmailNotSetUpException,
        is UserAlreadyExistsException -> 409

        is InsecurePasswordException -> 400

        else -> 500
    }

}
