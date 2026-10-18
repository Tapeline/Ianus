package dev.tapeline.ianus.application.usecases.toggle2fa

import dev.tapeline.ianus.application.exceptions.EmailNotSetUpException
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig
import org.springframework.stereotype.Component

data class Toggle2FADTO(
    val enable: Boolean
)

data class Toggle2FAResult(
    val enabled: Boolean
)

@Component
class Toggle2FAAction(
    override val users: UserStorage,
    private val tx: Transaction,
    override val userSessionEnvironment: UserSessionEnvironment,
    override val sessions: SessionStorage,
    override val clock: Clock
) : WithUserSessionEnvironment {

    // TODO: Refactor: add more ways for 2fa, add 2fa on 2fa disable, add 2fa recovery keys
    operator fun invoke(dto: Toggle2FADTO) = tx {
        withSignedUser { user ->
            if (dto.enable && user.email == null) {
                throw EmailNotSetUpException()
            }
            user.setSecondFactor(
                if (dto.enable) {
                    TwoFactorConfig.EMAIL
                } else {
                    TwoFactorConfig.DISABLED
                }
            )
                .also ( users::saveUser )
                .let { Toggle2FAResult(dto.enable) }
        }
    }

}