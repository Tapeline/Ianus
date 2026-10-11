package dev.tapeline.ianus.application.interfaces.environment

import dev.tapeline.ianus.domain.session.SessionToken

interface UserSessionEnvironment {

    fun getSessionToken(): SessionToken?

}
