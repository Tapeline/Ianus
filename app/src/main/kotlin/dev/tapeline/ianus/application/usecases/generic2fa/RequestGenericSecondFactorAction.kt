package dev.tapeline.ianus.application.usecases.generic2fa

import dev.tapeline.ianus.application.typeclasses.WithSecondFactorBeginVerification
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.issueAndSendCodeViaEmail
import dev.tapeline.ianus.application.typeclasses.requireSession
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.application.interfaces.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.email.compose2FAEmail
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import org.springframework.stereotype.Component

data class RequestGenericSecondFactorResult(
    val key: TwoFactorKey
)

@Component
class RequestGenericSecondFactorBeginAction(
    override val userSessionEnvironment: UserSessionEnvironment,
    override val sessions: SessionStorage,
    override val users: UserStorage,
    override val clock: Clock,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val emailSender: EmailSender,
    override val twoFactorConfig: TwoFactorConfiguration,
    val tx: Transaction
) : WithUserSessionEnvironment, WithSecondFactorBeginVerification {

    operator fun invoke() = tx {
        withSignedUser { user ->
            RequestGenericSecondFactorResult(
                issueAndSendCodeViaEmail(user, requireSession().origin, ::compose2FAEmail)
            )
        }
    }

}