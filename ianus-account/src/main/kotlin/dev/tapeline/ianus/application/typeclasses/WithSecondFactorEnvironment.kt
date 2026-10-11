package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.exceptions.SecondFactorNotPresentException
import dev.tapeline.ianus.application.interfaces.algorithms.PersistentSecondFactorEngine
import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.domain.user.User

interface WithSecondFactorEnvironment : WithUserSessionEnvironment {

    val secondFactorEnvironment: SecondFactorEnvironment
    val persistentSecondFactorEngine: PersistentSecondFactorEngine

}

fun WithSecondFactorEnvironment.requireSecondFactor() =
    secondFactorEnvironment.getSecondFactorToken()
        ?.let(persistentSecondFactorEngine::validateAndExtractSecondFactor)
        ?.apply {
            if (clock.now() > expiresAt)
                throw SecondFactorNotPresentException()
        }
        ?: throw SecondFactorNotPresentException()

fun <R> WithSecondFactorEnvironment.withSignedAnd2FA(f: (User) -> R) =
    withSignedUser { user ->
        if (requireSecondFactor().userId != user.id)
            throw SecondFactorNotPresentException()
        f(user)
    }

