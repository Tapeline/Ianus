package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class TwoFactorVerificationException : IanusException("CANNOT_VERIFY_2FA", "2FA verification exception")