package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class UnauthorisedException : IanusException("UNAUTHORISED", "Cannot perform this action")
