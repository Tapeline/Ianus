package dev.tapeline.ianus.application.interfaces.environment

import dev.tapeline.ianus.application.exceptions.UnauthenticatedException
import dev.tapeline.ianus.domain.user.UserPrincipal

interface UserAuthenticator {

    fun getAuthenticated(): UserPrincipal?

    fun requireAuthenticated() =
        getAuthenticated() ?: throw UnauthenticatedException()

}