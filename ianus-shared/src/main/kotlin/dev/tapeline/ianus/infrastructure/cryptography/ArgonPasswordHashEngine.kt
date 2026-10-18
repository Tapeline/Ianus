package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.PasswordHashEngine
import dev.tapeline.ianus.domain.HashedPassword
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component

@Component
class ArgonPasswordHashEngine : PasswordHashEngine {
    private val encoder = Argon2PasswordEncoder(16, 32, 1, 60000, 10)

    override fun hash(password: String) =
        password.let(encoder::encode).let(::HashedPassword)

    override fun hashValid(raw: String, hashed: HashedPassword) =
        encoder.matches(raw, hashed.value)

}