package dev.tapeline.ianus.infrastructure

import dev.tapeline.ianus.application.interfaces.environment.Clock
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RealClock : Clock {

    override fun now() =
        requireNotNull(Instant.now())

}
