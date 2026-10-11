package dev.tapeline.ianus.application.interfaces.environment

import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey

interface SecondFactorEnvironment {

    fun getSecondFactorToken(): SecondFactorToken?
    fun getTwoFactorKey(): TwoFactorKey?

}
