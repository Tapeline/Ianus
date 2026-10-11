package dev.tapeline.ianus.application.usecases.changeemail

import dev.tapeline.ianus.application.typeclasses.WithSecondFactorBeginVerification
import dev.tapeline.ianus.application.interfaces.*
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.issueAndSendCodeViaEmail
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.email.composeEmailChangeEmail
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.domain.user.Email
import org.springframework.stereotype.Component

data class BeginChangeEmailDTO(
    val email: Email,
    val origin: SessionOrigin
)

data class BeginChangeEmailResult(
    val key: TwoFactorKey
)

@Component
class BeginChangeEmailAction(
    override val users: UserStorage,
    private val tx: Transaction,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val emailSender: EmailSender,
    override val userSessionEnvironment: UserSessionEnvironment,
    override val sessions: SessionStorage,
    override val clock: Clock,
    override val twoFactorConfig: TwoFactorConfiguration
) : WithUserSessionEnvironment, WithSecondFactorBeginVerification {

    operator fun invoke(dto: BeginChangeEmailDTO) = tx {
        withSignedUser { user ->
            issueAndSendCodeViaEmail(user, dto.email.value, dto.origin, ::composeEmailChangeEmail)
                .let(::BeginChangeEmailResult)
        }
    }

}
