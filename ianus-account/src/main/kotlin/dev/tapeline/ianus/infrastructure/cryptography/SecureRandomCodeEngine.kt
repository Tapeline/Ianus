package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.application.interfaces.algorithms.SecureCodeEngine
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class SecureRandomCodeEngine : SecureCodeEngine {

    override fun generateRandom6DigitCode() =
        SecureRandom().nextInt(0, 1000000).toString().padStart(6, '0')

}
