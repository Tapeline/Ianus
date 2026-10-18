package dev.tapeline.ianus.application.interfaces.algorithms

import dev.tapeline.ianus.domain.authcode.AuthorisationCode
import dev.tapeline.ianus.domain.authcode.AuthorisationCodeClaims

interface AuthorisationCodeEngine {

    fun createAuthorisationCode(claims: AuthorisationCodeClaims): AuthorisationCode
    fun extractClaims(code: AuthorisationCode): AuthorisationCodeClaims

}
