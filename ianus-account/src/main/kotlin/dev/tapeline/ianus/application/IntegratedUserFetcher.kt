package dev.tapeline.ianus.application

import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.environment.UserAuthenticator
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserFetcher
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.getSignedUser
import dev.tapeline.ianus.domain.user.UserId
import dev.tapeline.ianus.domain.user.UserPrincipal
import org.springframework.stereotype.Component

@Component
class IntegratedUserFetcher(
    private val users: UserStorage
) : UserFetcher {

    override fun getUser(userId: UserId) =
        users.getUser(userId)?.run {
            UserPrincipal(id, username, email, twoFactor)
        }

}
