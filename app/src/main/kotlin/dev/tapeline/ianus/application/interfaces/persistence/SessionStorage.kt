package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.session.Session
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.user.UserId

interface SessionStorage {

    fun getSession(sessionId: SessionId): Session?
    fun getSession(sessionToken: SessionToken): Session?
    fun createSession(session: Session)
    fun revokeSession(sessionId: SessionId)
    fun getActiveSessions(userId: UserId): List<Session>

}