package dev.tapeline.ianus.application.interfaces.algorithms

import dev.tapeline.ianus.domain.twofactor.SecondFactor
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken

/**
 * Handles longer-lived 2FA flow: after 2FA has succeeded through
 * flow that [TwoFactorVerificationEngine] handles,
 * [PersistentSecondFactorEngine] can be used to generate a signature
 * that persists the fact of successfully passing the 2FA flow
 * (and then verifying the token).
 */
interface PersistentSecondFactorEngine {

    fun issueSecondFactorToken(secondFactor: SecondFactor): SecondFactorToken
    fun validateAndExtractSecondFactor(token: SecondFactorToken): SecondFactor

}
