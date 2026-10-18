package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.user.UserId
import dev.tapeline.ianus.domain.user.UserPrincipal

interface UserFetcher {

    fun getUser(userId: UserId): UserPrincipal?

}
