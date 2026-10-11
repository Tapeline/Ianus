package dev.tapeline.ianus.infrastructure.email

import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.domain.EmailMessage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["app.email-sender"], havingValue = "smtp")
class SmtpEmailSender(
    private val emailSender: JavaMailSender
) : EmailSender {

    override fun sendEmail(email: EmailMessage) =
        SimpleMailMessage()
            .apply {
                from = "noreply-iam@tapeline.dev"
                setTo(email.email)
                subject = email.subject
                text = email.content
            }
            .let(emailSender::send)

}
