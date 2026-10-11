package dev.tapeline.ianus.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Path

enum class MailBackendType {
    SMTP,
    LOGFILE
}

@ConfigurationProperties("app.mail")
data class MailConfiguration(
    val backend: MailBackendType
)

sealed interface MailBackendConfiguration

@ConfigurationProperties("app.mail.config")
@ConditionalOnProperty(value = ["app.mail.backend"], havingValue = "smtp")
data class SmtpBackendConfiguration(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val authenticate: Boolean
) : MailBackendConfiguration

@ConfigurationProperties("app.mail.config")
@ConditionalOnProperty(value = ["app.mail.backend"], havingValue = "logfile")
data class LogfileBackendConfiguration(
    val filename: Path
) : MailBackendConfiguration
