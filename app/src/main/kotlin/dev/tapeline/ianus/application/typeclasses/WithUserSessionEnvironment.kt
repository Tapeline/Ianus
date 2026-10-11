package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.exceptions.UnauthenticatedException
import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.domain.user.User

interface WithUserSessionEnvironment {

    val userSessionEnvironment: UserSessionEnvironment
    val sessions: SessionStorage
    val users: UserStorage
    val clock: Clock

}

fun <R> WithUserSessionEnvironment.withSignedUser(f: (User) -> R) =
    f(requireSignedUser())

fun WithUserSessionEnvironment.getSession() =
    userSessionEnvironment.getSessionToken()
        ?.let(sessions::getSession)

fun WithUserSessionEnvironment.requireSession() =
    getSession() ?: throw UnauthenticatedException()

fun WithUserSessionEnvironment.getSignedUser() =
    getSession()
        ?.run {
            if (clock.now() > expiresAt)
                throw UnauthenticatedException()
            users.getUser(userId)
        }

fun WithUserSessionEnvironment.requireSignedUser() =
    getSignedUser() ?: throw UnauthenticatedException()
