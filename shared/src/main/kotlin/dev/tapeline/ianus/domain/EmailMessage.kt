package dev.tapeline.ianus.domain

data class EmailMessage(
    val email: String,
    val subject: String,
    val content: String,
)