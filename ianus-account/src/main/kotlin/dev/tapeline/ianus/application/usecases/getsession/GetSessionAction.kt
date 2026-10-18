package dev.tapeline.ianus.application.usecases.getsession

import dev.tapeline.ianus.application.exceptions.NotFoundException
import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.application.interfaces.environment.UserSessionEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.application.typeclasses.WithUserSessionEnvironment
import dev.tapeline.ianus.application.typeclasses.withSignedUser
import dev.tapeline.ianus.domain.session.SessionId
import org.springframework.stereotype.Component

@Component
class GetSessionAction(
    private val tx: Transaction,
    override val userSessionEnvironment: UserSessionEnvironment,
    override val sessions: SessionStorage,
    override val users: UserStorage,
    override val clock: Clock
) : WithUserSessionEnvironment {

    operator fun invoke(sessionId: SessionId) = tx {
        withSignedUser { user ->
            sessions.getSession(sessionId)
                ?.takeIf { it.userId == user.id }
                ?: throw NotFoundException()
        }
    }

}
