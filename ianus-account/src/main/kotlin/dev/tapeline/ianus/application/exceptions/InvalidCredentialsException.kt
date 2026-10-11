package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class InvalidCredentialsException : IanusException("INVALID_CREDENTIALS", "Invalid credentials")
