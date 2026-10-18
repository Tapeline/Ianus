package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.app.App
import dev.tapeline.ianus.domain.app.AppId

interface AppRepository {

    fun getApp(appId: AppId): App?

}
