package dev.tapeline.ianus.application.interfaces.algorithms

import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.domain.twofactor.TwoFactorKeyClaims

/**
 * Handles short-lived 2FA flow, where the authenticity of
 * the user is finally confirmed by them supplying back
 * to the system both the code and the key.
 */
interface TwoFactorVerificationEngine {

    fun createTwoFactorPair(claims: TwoFactorKeyClaims): Pair<TwoFactorCode, TwoFactorKey>
    fun extractClaims(key: TwoFactorKey): TwoFactorKeyClaims

}