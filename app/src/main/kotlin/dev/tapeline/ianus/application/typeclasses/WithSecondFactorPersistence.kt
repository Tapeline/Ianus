package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.algorithms.PersistentSecondFactorEngine
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.twofactor.SecondFactor
import dev.tapeline.ianus.domain.user.UserId

interface WithSecondFactorPersistence {

    val clock: Clock
    val twoFactorConfig: TwoFactorConfiguration
    val persistentSecondFactorEngine: PersistentSecondFactorEngine

}

fun WithSecondFactorPersistence.createPersistentSecondFactor(userId: UserId) =
    persistentSecondFactorEngine.issueSecondFactorToken(
        SecondFactor(userId, clock.now(), twoFactorConfig.persistentTtl)
    )
