package dev.tapeline.ianus.presentation.security

import dev.tapeline.ianus.application.interfaces.environment.SecondFactorEnvironment
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpringSecondFactorEnvironment : SecondFactorEnvironment {

    override fun getSecondFactorToken() =
        (SecurityContextHolder.getContext().authentication as? IanusAuthentication)?.secondFactorToken

    override fun getTwoFactorKey() =
        (SecurityContextHolder.getContext().authentication as? IanusAuthentication)?.twoFactorKey

}
