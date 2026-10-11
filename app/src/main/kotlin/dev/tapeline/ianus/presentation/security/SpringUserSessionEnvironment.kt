package dev.tapeline.ianus.presentation.security

import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class SpringUserSessionEnvironment : UserSessionEnvironment {

    override fun getSessionToken() =
        (SecurityContextHolder.getContext().authentication as? IanusAuthentication)?.sessionToken

}
