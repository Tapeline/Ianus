package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class RegistrationDisabledException : IanusException("REGISTRATION_DISABLED", "Registration is disabled")