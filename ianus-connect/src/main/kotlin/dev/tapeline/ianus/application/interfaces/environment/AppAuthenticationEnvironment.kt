package dev.tapeline.ianus.application.interfaces.environment

import dev.tapeline.ianus.domain.app.AppId

interface AppAuthenticationEnvironment {

    fun getAppId(): AppId?
    fun getRawAppSecret(): String?

}
