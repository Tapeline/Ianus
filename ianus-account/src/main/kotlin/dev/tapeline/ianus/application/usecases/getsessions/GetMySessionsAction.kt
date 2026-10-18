package dev.tapeline.ianus.application.usecases.getsessions

import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import org.springframework.stereotype.Component

@Component
class GetMySessionsAction(
    private val tx: Transaction,
    override val userSessionEnvironment: UserSessionEnvironment,
    override val sessions: SessionStorage,
    override val users: UserStorage,
    override val clock: Clock
) : WithUserSessionEnvironment {

    operator fun invoke() = tx {
        withSignedUser { user ->
            sessions.getActiveSessions(user.id)
        }
    }

}
