package dev.tapeline.ianus.domain.user

import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig

data class UserPrincipal(
    val id: UserId,
    val username: Username,
    val email: Email?,
    val twoFactor: TwoFactorConfig,
)
