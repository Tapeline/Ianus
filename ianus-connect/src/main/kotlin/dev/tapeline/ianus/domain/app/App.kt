package dev.tapeline.ianus.domain.app

import dev.tapeline.ianus.domain.HashedPassword
import dev.tapeline.ianus.domain.scope.Scope
import java.net.URI

data class App(
    val id: AppId,
    val name: AppName,
    val redirectUri: URI,
    val scopes: Set<Scope>,
    val secret: HashedPassword
)
