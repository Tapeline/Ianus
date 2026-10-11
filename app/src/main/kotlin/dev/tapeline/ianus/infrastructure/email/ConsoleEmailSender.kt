package dev.tapeline.ianus.infrastructure.email

import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.domain.EmailMessage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["app.email-sender"], havingValue = "console")
class ConsoleEmailSender : EmailSender {

    override fun sendEmail(email: EmailMessage) =
        println(email)

}