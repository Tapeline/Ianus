package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.user.User
import dev.tapeline.ianus.domain.user.UserId
import dev.tapeline.ianus.domain.user.Username

interface UserStorage {

    fun getUser(username: Username): User?
    fun getUser(id: UserId): User?
    fun saveUser(user: User): Unit
    fun createUser(user: User): Unit

}