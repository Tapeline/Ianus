package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.permissions.Permission
import dev.tapeline.ianus.domain.user.UserId

interface PermissionsStorage {

    fun getUserPermissions(id: UserId): List<Permission>

}