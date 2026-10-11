package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class SecondFactorNotPresentException : IanusException("2FA_REQUIRED", "2FA required")