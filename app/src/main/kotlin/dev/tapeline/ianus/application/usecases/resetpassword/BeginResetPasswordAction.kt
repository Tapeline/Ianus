package dev.tapeline.ianus.application.usecases.resetpassword

import dev.tapeline.ianus.application.exceptions.InvalidCredentialsException
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorBeginVerification
import dev.tapeline.ianus.application.typeclasses.issueAndSendCodeViaEmail
import dev.tapeline.ianus.application.interfaces.*
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.email.composePasswordRecoveryEmail
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.domain.user.Email
import dev.tapeline.ianus.domain.user.Username
import org.springframework.stereotype.Component

data class BeginResetPasswordDTO(
    val username: Username,
    val email: Email,
    val origin: SessionOrigin
)

data class BeginResetPasswordResult(
    val key: TwoFactorKey
)

@Component
class BeginResetPasswordAction(
    private val users: UserStorage,
    private val tx: Transaction,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val emailSender: EmailSender,
    override val clock: Clock,
    override val twoFactorConfig: TwoFactorConfiguration
) : WithSecondFactorBeginVerification {

    operator fun invoke(dto: BeginResetPasswordDTO) = tx {
        users.getUser(dto.username)
            ?.takeIf { it.email?.value == dto.email.value }
            ?.let { user ->
                issueAndSendCodeViaEmail(user, dto.origin, ::composePasswordRecoveryEmail)
                    .let(::BeginResetPasswordResult)
            }
            ?: throw InvalidCredentialsException()
    }

}
