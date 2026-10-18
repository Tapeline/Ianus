package dev.tapeline.ianus.application.interfaces.algorithms

import dev.tapeline.ianus.domain.HashedPassword

interface PasswordHashEngine {

    fun hash(password: String): HashedPassword
    fun hashValid(raw: String, hashed: HashedPassword): Boolean

}
