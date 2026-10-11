package dev.tapeline.ianus.domain.email

import dev.tapeline.ianus.domain.EmailMessage

const val HEADER = "Hello, this is an automated message from Ianus Identity and Authenticity Management System."
const val FOOTER = "--- Ianus Identity and Authenticity Management System"

fun compose2FAEmail(email: String, code: String) =
    EmailMessage(
        email = email,
        subject = "Ianus 2FA",
        content = """
            $HEADER
            
            This is your second factor verification code:
           
            ======
            $code
            ======
            
            If you did not request this code, reset your password as quickly as possible:
            https://iam.tapeline.dev/my/reset-password
            
            $FOOTER
        """.trimIndent()
    )

fun composePasswordRecoveryEmail(email: String, code: String) =
    EmailMessage(
        email = email,
        subject = "Ianus 2FA",
        content = """
            $HEADER
            
            This is your password recovery confirmation code
           
            ======
            $code
            ======
            
            If you did not request this code, do not give this code to anyone 
            and dispose of this message.
            
            $FOOTER
        """.trimIndent()
    )

fun composeEmailChangeEmail(email: String, code: String) =
    EmailMessage(
        email = email,
        subject = "Ianus 2FA",
        content = """
            $HEADER
            
            This is your email confirmation code
           
            ======
            $code
            ======
            
            If you did not request this code, do not give this code to anyone 
            and dispose of this message.
            
            $FOOTER
        """.trimIndent()
    )
