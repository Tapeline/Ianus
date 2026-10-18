package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.exceptions.AppUnauthenticatedException
import dev.tapeline.ianus.application.interfaces.algorithms.PasswordHashEngine
import dev.tapeline.ianus.application.interfaces.environment.AppAuthenticationEnvironment
import dev.tapeline.ianus.application.interfaces.persistence.AppRepository
import dev.tapeline.ianus.domain.app.App

interface WithAppAuthenticationEnvironment {

    val appAuthenticationEnvironment: AppAuthenticationEnvironment
    val apps: AppRepository
    val passwordHashEngine: PasswordHashEngine

}

fun WithAppAuthenticationEnvironment.getApp() =
    appAuthenticationEnvironment.getAppId()
        ?.let(apps::getApp)

fun WithAppAuthenticationEnvironment.getSignedApp() =
    getApp()?.let { app ->
        appAuthenticationEnvironment.getRawAppSecret()?.let {
            if (passwordHashEngine.hashValid(it, app.secret))
                app
            else
                null
        }
    }

fun WithAppAuthenticationEnvironment.requireSignedApp() =
    getSignedApp() ?: throw AppUnauthenticatedException()

fun <R> WithAppAuthenticationEnvironment.withSignedApp(f: (App) -> R) =
    f(requireSignedApp())
