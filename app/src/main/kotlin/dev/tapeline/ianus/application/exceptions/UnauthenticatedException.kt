package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class UnauthenticatedException : IanusException("UNAUTHENTICATED", "Unauthenticated")