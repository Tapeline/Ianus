package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.interfaces.environment.Clock
import dev.tapeline.ianus.application.interfaces.algorithms.SecureKeyEngine
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.common.newId
import dev.tapeline.ianus.domain.session.Session
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.user.User

interface WithSessionCreation {

    val clock: Clock
    val sessions: SessionStorage
    val secureKeyEngine: SecureKeyEngine

}

fun WithSessionCreation.createSession(user: User, origin: SessionOrigin) = 
    newId(::SessionId)
        .let { id ->
            Session(
                id = id,
                token = SessionToken(id.value + "-" + secureKeyEngine.generateSessionSecret()),
                userId = user.id,
                origin = origin,
                createdAt = clock.now(),
                ttl = user.sessionTtl
            )
        }
        .also(sessions::createSession)
