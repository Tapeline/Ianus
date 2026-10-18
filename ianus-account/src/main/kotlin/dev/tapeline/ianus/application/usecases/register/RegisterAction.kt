package dev.tapeline.ianus.application.usecases.register

import dev.tapeline.ianus.application.exceptions.RegistrationDisabledException
import dev.tapeline.ianus.application.exceptions.UserAlreadyExistsException
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.PasswordHashEngine
import dev.tapeline.ianus.application.interfaces.algorithms.SecureKeyEngine
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithSessionCreation
import dev.tapeline.ianus.application.typeclasses.createSession
import dev.tapeline.ianus.application.usecases.ensurePasswordValid
import dev.tapeline.ianus.common.newId
import dev.tapeline.ianus.configuration.RegistrationConfiguration
import dev.tapeline.ianus.configuration.SessionConfiguration
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig
import dev.tapeline.ianus.domain.HashedPassword
import dev.tapeline.ianus.domain.user.User
import dev.tapeline.ianus.domain.user.UserId
import dev.tapeline.ianus.domain.user.Username
import org.springframework.stereotype.Component
import java.time.Duration

data class RegisterDTO(
    val username: Username,
    val rawPassword: String,
    val origin: SessionOrigin,
)

data class RegisterResult(
    val userId: UserId,
    val sessionId: SessionId,
    val sessionToken: SessionToken,
    val sessionTtl: Duration,
)

@Component
class RegisterAction(
    private val users: UserStorage,
    override val sessions: SessionStorage,
    private val tx: Transaction,
    private val hash: PasswordHashEngine,
    override val secureKeyEngine: SecureKeyEngine,
    override val clock: Clock,
    private val sessionConfig: SessionConfiguration,
    private val registrationConfig: RegistrationConfiguration,
) : WithSessionCreation {
    operator fun invoke(dto: RegisterDTO) =
        if (!registrationConfig.enabled) {
            throw RegistrationDisabledException()
        } else {
            registrationConfig.ensurePasswordValid(dto.rawPassword)
            runRegistration(dto)
        }

    private fun runRegistration(dto: RegisterDTO) = tx {
        users.getUser(dto.username)
            ?.let { throw UserAlreadyExistsException() }
            ?: freshUser(dto.username, hash.hash(dto.rawPassword))
                .also(users::createUser)
                .let { user ->
                    createSession(user, dto.origin)
                        .run { RegisterResult(userId, id, token, ttl) }
                }
    }

    private fun freshUser(username: Username, hashedPassword: HashedPassword) =
        User(
            id = newId(::UserId),
            username = username,
            password = hashedPassword,
            email = null,
            twoFactor = TwoFactorConfig.DISABLED,
            sessionTtl = sessionConfig.defaultSessionTtl
        )

}
