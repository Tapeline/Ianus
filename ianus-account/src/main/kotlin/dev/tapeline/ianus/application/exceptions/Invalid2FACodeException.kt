package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class Invalid2FACodeException : IanusException("INVALID_2FA_CODE", "Invalid 2FA code")