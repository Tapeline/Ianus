package dev.tapeline.ianus.infrastructure.storage

import dev.tapeline.ianus.application.interfaces.persistence.AppRepository
import dev.tapeline.ianus.configuration.OidcAppsConfiguration
import dev.tapeline.ianus.domain.HashedPassword
import dev.tapeline.ianus.domain.app.App
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.app.AppName
import dev.tapeline.ianus.domain.scope.Scope
import org.springframework.stereotype.Component

@Component
class SpringAppRepository(
    private val appConfig: OidcAppsConfiguration
) : AppRepository {

    override fun getApp(appId: AppId) =
        appConfig.apps.firstOrNull { it.id == appId.value }?.run {
            App(
                id = appId,
                name = AppName(name),
                redirectUri = redirectUri,
                scopes = scopes.map(::Scope).toSet(),
                secret = HashedPassword(secret)
            )
        }

}
