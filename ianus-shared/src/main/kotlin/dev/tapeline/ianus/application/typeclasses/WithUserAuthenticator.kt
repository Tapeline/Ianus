package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.interfaces.environment.UserAuthenticator
import dev.tapeline.ianus.domain.user.UserPrincipal

interface WithUserAuthenticator {

    val userAuthenticator: UserAuthenticator

}

fun <R> WithUserAuthenticator.withSignedUser(f: (UserPrincipal) -> R) =
    f(userAuthenticator.requireAuthenticated())
