package dev.tapeline.ianus.domain.user

import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig
import java.time.Duration


data class User(
    val id: UserId,
    val username: Username,
    val password: HashedPassword,
    val email: Email?,
    val twoFactor: TwoFactorConfig,
    val sessionTtl: Duration
) {
    fun changePassword(newPassword: HashedPassword) =
        User(id, username, newPassword, email, twoFactor, sessionTtl)

    fun setSecondFactor(factor: TwoFactorConfig) =
        require(factor == TwoFactorConfig.DISABLED || email != null) { "Email cannot be null when enabling twofactor" }
            .let { User(id, username, password, email, factor, sessionTtl) }

    fun changeEmail(newEmail: Email) =
        User(id, username, password, newEmail, twoFactor, sessionTtl)
}
