package dev.tapeline.ianus.application.usecases.login

import dev.tapeline.ianus.application.exceptions.InvalidCredentialsException
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorBeginVerification
import dev.tapeline.ianus.application.typeclasses.WithSessionCreation
import dev.tapeline.ianus.application.typeclasses.createSession
import dev.tapeline.ianus.application.typeclasses.issueAndSendCodeViaEmail
import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.algorithms.PasswordHashEngine
import dev.tapeline.ianus.application.interfaces.algorithms.SecureKeyEngine
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.domain.user.UserId
import dev.tapeline.ianus.domain.user.Username
import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.email.compose2FAEmail
import dev.tapeline.ianus.domain.session.SessionToken
import org.springframework.stereotype.Component
import java.time.Duration

data class LoginDTO(
    val username: Username,
    val rawPassword: String,
    val origin: SessionOrigin,
)

sealed class LoginResult(
    open val userId: UserId
) {
    data class Requires2FA(
        override val userId: UserId,
        val twoFactorKey: TwoFactorKey,
    ) : LoginResult(userId)

    data class Successful(
        override val userId: UserId,
        val sessionId: SessionId,
        val sessionToken: SessionToken,
        val sessionTtl: Duration
    ) : LoginResult(userId)
}

@Component
class BeginLoginAction(
    private val users: UserStorage,
    override val sessions: SessionStorage,
    private val tx: Transaction,
    private val hash: PasswordHashEngine,
    override val secureKeyEngine: SecureKeyEngine,
    override val clock: Clock,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val emailSender: EmailSender,
    override val twoFactorConfig: TwoFactorConfiguration
) : WithSecondFactorBeginVerification, WithSessionCreation {
    operator fun invoke(dto: LoginDTO) = tx {
        users.getUser(dto.username)
            ?.takeIf { hash.hashValid(dto.rawPassword, it.password) }
            ?.let { user ->
                when (user.twoFactor) {
                    TwoFactorConfig.EMAIL -> LoginResult.Requires2FA(
                        user.id,
                        issueAndSendCodeViaEmail(user, dto.origin, ::compose2FAEmail)
                    )

                    TwoFactorConfig.DISABLED -> createSession(user, dto.origin)
                        .run { LoginResult.Successful(userId, id, token, ttl) }
                }
            } ?: throw InvalidCredentialsException()
    }
}