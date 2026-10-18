package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.exceptions.Invalid2FACodeException
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.twofactor.TwoFactorKeyClaims
import dev.tapeline.ianus.domain.user.UserId

interface WithSecondFactorCompleteVerification {

    val twoFactorVerificationEngine: TwoFactorVerificationEngine
    val secondFactorEnvironment: SecondFactorEnvironment

}

fun <R> WithSecondFactorCompleteVerification.withValidKeyClaims(
    userId: UserId,
    code: TwoFactorCode,
    block: (TwoFactorKeyClaims) -> R
) =
    withValidKeyClaims(code) { claims ->
        if (claims.userId != userId)
            throw Invalid2FACodeException()
        block(claims)
    }

fun <R> WithSecondFactorCompleteVerification.withValidKeyClaims(
    code: TwoFactorCode,
    block: (TwoFactorKeyClaims) -> R
) =
    secondFactorEnvironment.getTwoFactorKey()
        ?.let { key ->
            val claims = twoFactorVerificationEngine.extractClaims(key)
            if (claims.code != code)
                throw Invalid2FACodeException()
            block(claims)
        }
        ?: throw Invalid2FACodeException()
