package dev.tapeline.ianus.presentation.controllers

import dev.tapeline.ianus.application.usecases.getsession.GetSessionAction
import dev.tapeline.ianus.application.usecases.getsessions.GetMySessionsAction
import dev.tapeline.ianus.application.usecases.revokeallsessions.RevokeAllMySessionsAction
import dev.tapeline.ianus.application.usecases.revokesession.RevokeSessionAction
import dev.tapeline.ianus.domain.session.Session
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.presentation.openapi.api.SessionsApi
import dev.tapeline.ianus.presentation.openapi.model.SessionResponse
import org.springframework.beans.factory.ObjectProvider
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import javax.validation.constraints.NotNull

@RestController
class SessionsController(
    private val getSessionActionProvider: ObjectProvider<GetSessionAction>,
    private val getMySessionsActionProvider: ObjectProvider<GetMySessionsAction>,
    private val revokeSessionActionProvider: ObjectProvider<RevokeSessionAction>,
    private val revokeAllMySessionsActionProvider: ObjectProvider<RevokeAllMySessionsAction>,
) : SessionsApi {

    override fun getApiV1MySessions() =
        getMySessionsActionProvider.getObject()()
            .map(Session::toSessionResponse)
            .toTypedArray()
            .to200OK()

    override fun deleteApiV1MySessions() =
        revokeAllMySessionsActionProvider.getObject()()
            .to204NoContent()

    override fun getApiV1MySessionsSessionId(sessionId: @NotNull String) =
        getSessionActionProvider.getObject()(SessionId(sessionId))
            .let(Session::toSessionResponse)
            .to200OK()

    override fun deleteApiV1MySessionsSessionId(sessionId: @NotNull String) =
        revokeSessionActionProvider.getObject()(SessionId(sessionId))
            .to204NoContent()

}

private fun Session.toSessionResponse() =
    SessionResponse(
        id.value,
        origin.value,
        createdAt.atOffset(ZoneOffset.UTC),
        ttl.toMillis().toInt(),
    )
