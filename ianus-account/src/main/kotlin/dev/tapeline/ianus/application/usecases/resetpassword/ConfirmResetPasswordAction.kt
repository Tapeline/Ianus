package dev.tapeline.ianus.application.usecases.resetpassword

import dev.tapeline.ianus.application.exceptions.Invalid2FACodeException
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.algorithms.PasswordHashEngine
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithSecondFactorCompleteVerification
import dev.tapeline.ianus.application.typeclasses.withValidKeyClaims
import dev.tapeline.ianus.application.usecases.ensurePasswordValid
import dev.tapeline.ianus.configuration.RegistrationConfiguration
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import org.springframework.stereotype.Component

data class ConfirmResetPasswordDTO(
    val code: TwoFactorCode,
    val newRawPassword: String
)

@Component
class ConfirmResetPasswordAction(
    private val users: UserStorage,
    private val hash: PasswordHashEngine,
    private val tx: Transaction,
    override val twoFactorVerificationEngine: TwoFactorVerificationEngine,
    private val registrationConfig: RegistrationConfiguration,
    override val secondFactorEnvironment: SecondFactorEnvironment,
) : WithSecondFactorCompleteVerification {

    operator fun invoke(dto: ConfirmResetPasswordDTO) = tx {
        registrationConfig.ensurePasswordValid(dto.newRawPassword)
        withValidKeyClaims(dto.code) { claims ->
            users.getUser(claims.userId)
                ?.apply { changePassword(hash.hash(dto.newRawPassword)) }
                ?.also(users::saveUser)
                ?: throw Invalid2FACodeException()
        }
    }

}
