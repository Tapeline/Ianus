package dev.tapeline.ianus.application.usecases.login

import dev.tapeline.ianus.application.exceptions.Invalid2FACodeException
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorPersistence
import dev.tapeline.ianus.application.typeclasses.WithSessionCreation
import dev.tapeline.ianus.application.typeclasses.createPersistentSecondFactor
import dev.tapeline.ianus.application.typeclasses.createSession
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.PersistentSecondFactorEngine
import dev.tapeline.ianus.application.interfaces.algorithms.SecureKeyEngine
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorCompleteVerification
import dev.tapeline.ianus.application.typeclasses.withValidKeyClaims
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.user.UserId
import org.springframework.stereotype.Component
import java.time.Duration

data class TwoFactorCodeDTO(
    val code: TwoFactorCode,
)

data class Successful2FALoginResult(
    val userId: UserId,
    val sessionId: SessionId,
    val sessionToken: SessionToken,
    val sessionTtl: Duration,
    val persistent2FA: SecondFactorToken,
)

@Component
class ConfirmTwoFactorLoginAction(
    private val users: UserStorage,
    override val sessions: SessionStorage,
    private val tx: Transaction,
    override val clock: Clock,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    override val secureKeyEngine: SecureKeyEngine,
    override val twoFactorConfig: TwoFactorConfiguration,
    override val persistentSecondFactorEngine: PersistentSecondFactorEngine,
    override val secondFactorEnvironment: SecondFactorEnvironment,
) : WithSessionCreation, WithSecondFactorPersistence, WithSecondFactorCompleteVerification {

    operator fun invoke(dto: TwoFactorCodeDTO) = tx {
        withValidKeyClaims(dto.code) { claims ->
            users.getUser(claims.userId)
                ?.let { user ->
                    val session = createSession(user, claims.loginOrigin)
                    Successful2FALoginResult(
                        user.id,
                        session.id,
                        session.token,
                        user.sessionTtl,
                        createPersistentSecondFactor(user.id)
                    )
                }
                ?: throw Invalid2FACodeException()
        }
    }

}