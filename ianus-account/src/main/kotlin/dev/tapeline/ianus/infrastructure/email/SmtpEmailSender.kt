package dev.tapeline.ianus.infrastructure.email

import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.configuration.SmtpBackendConfiguration
import dev.tapeline.ianus.domain.EmailMessage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["app.mail.backend"], havingValue = "smtp")
class SmtpEmailSender(
    private val config: SmtpBackendConfiguration,
) : EmailSender {

    private val emailSender = JavaMailSenderImpl().apply {
        host = config.host
        port = config.port
        username = config.username
        password = config.password
        javaMailProperties.apply {
            put("mail.transport.protocol", "smtp")
            put("mail.smtp.auth", if (config.authenticate) "true" else "false")
            put("mail.smtp.starttls.enable", "true")
            put("mail.debug", "true")
        }
    }

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
