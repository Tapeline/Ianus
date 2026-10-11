package dev.tapeline.ianus.application.usecases.changeemail

import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorCompleteVerification
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.application.typeclasses.withValidKeyClaims
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.user.Email
import org.springframework.stereotype.Component

data class ConfirmChangeEmailDTO(
    val code: TwoFactorCode,
    val email: Email,
)

data class ConfirmChangeEmailResult(
    val email: Email,
)

@Component
class ConfirmChangeEmailAction(
    override val userSessionEnvironment: UserSessionEnvironment,
    override val users: UserStorage,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val sessions: SessionStorage,
    override val clock: Clock,
    override val secondFactorEnvironment: SecondFactorEnvironment,
    private val tx: Transaction,
) : WithUserSessionEnvironment, WithSecondFactorCompleteVerification {

    operator fun invoke(dto: ConfirmChangeEmailDTO) = tx {
        withSignedUser { user ->
            withValidKeyClaims(user.id, dto.code) {
                user.changeEmail(dto.email)
                    .also(users::saveUser)
                    .let { ConfirmChangeEmailResult(dto.email) }
            }
        }
    }

}
