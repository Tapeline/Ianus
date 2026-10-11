package dev.tapeline.ianus.application.interfaces

import dev.tapeline.ianus.domain.EmailMessage

interface EmailSender {

    fun sendEmail(email: EmailMessage)

}