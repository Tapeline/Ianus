package dev.tapeline.ianus.presentation

import dev.tapeline.ianus.application.interfaces.environment.AppAuthenticationEnvironment
import dev.tapeline.ianus.domain.app.AppId

class RequestAppAuthenticationEnvironment(
    private val appId: AppId?,
    private val rawAppSecret: String?
) : AppAuthenticationEnvironment {
    override fun getAppId() = appId
    override fun getRawAppSecret() = rawAppSecret
}
