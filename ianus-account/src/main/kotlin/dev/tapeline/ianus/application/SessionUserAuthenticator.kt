package dev.tapeline.ianus.application

import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.environment.UserAuthenticator
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.getSignedUser
import dev.tapeline.ianus.domain.user.UserPrincipal
import org.springframework.stereotype.Component

@Component
class SessionUserAuthenticator(
    override val userSessionEnvironment: UserSessionEnvironment,
    override val sessions: SessionStorage,
    override val users: UserStorage,
    override val clock: Clock
) : UserAuthenticator, WithUserSessionEnvironment {

    override fun getAuthenticated() =
        getSignedUser()?.run {
            UserPrincipal(id, username, email, twoFactor)
        }

}
