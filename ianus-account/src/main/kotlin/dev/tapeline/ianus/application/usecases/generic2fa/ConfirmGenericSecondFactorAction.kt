package dev.tapeline.ianus.application.usecases.generic2fa

import dev.tapeline.ianus.application.typeclasses.WithSecondFactorPersistence
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.createPersistentSecondFactor
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.PersistentSecondFactorEngine
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorCompleteVerification
import dev.tapeline.ianus.application.typeclasses.withValidKeyClaims
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import org.springframework.stereotype.Component

data class ConfirmGenericSecondFactorDTO(
    val code: TwoFactorCode,
)

data class ConfirmGenericSecondFactorResult(
    val token: SecondFactorToken
)

@Component
class ConfirmGenericSecondFactorAction(
    override val userSessionEnvironment: UserSessionEnvironment,
    override val secondFactorEnvironment: SecondFactorEnvironment,
    override val sessions: SessionStorage,
    override val users: UserStorage,
    override val clock: Clock,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val twoFactorConfig: TwoFactorConfiguration,
    override val persistentSecondFactorEngine: PersistentSecondFactorEngine,
    val tx: Transaction,
) : WithUserSessionEnvironment, WithSecondFactorCompleteVerification, WithSecondFactorPersistence {

    operator fun invoke(dto: ConfirmGenericSecondFactorDTO) = tx {
        withSignedUser { user ->
            withValidKeyClaims(user.id, dto.code) {
                ConfirmGenericSecondFactorResult(createPersistentSecondFactor(user.id))
            }
        }
    }

}