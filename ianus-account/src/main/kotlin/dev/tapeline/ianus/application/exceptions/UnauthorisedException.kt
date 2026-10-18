package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class UnauthorisedException : IanusException("UNAUTHORISED", "Cannot perform this action")
