package dev.tapeline.ianus.infrastructure.email

import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.configuration.LogfileBackendConfiguration
import dev.tapeline.ianus.domain.EmailMessage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.StandardOpenOption

@Component
@ConditionalOnProperty(name = ["app.mail.backend"], havingValue = "logfile")
class LogfileEmailSender(
    private val config: LogfileBackendConfiguration
) : EmailSender {

    override fun sendEmail(email: EmailMessage) {
        Files.writeString(
            config.filename,
            email.content,
            Charsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        )
    }

}
