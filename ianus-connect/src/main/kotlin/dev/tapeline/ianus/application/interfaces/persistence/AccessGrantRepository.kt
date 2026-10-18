package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.grant.AccessGrant
import dev.tapeline.ianus.domain.grant.AccessId
import dev.tapeline.ianus.domain.grant.AccessToken

interface AccessGrantRepository {

    fun getGrant(id: AccessId): AccessGrant?
    fun getGrant(token: AccessToken): AccessGrant?
    fun createGrant(grant: AccessGrant): Unit
    fun updateGrant(grant: AccessGrant): Unit

}
