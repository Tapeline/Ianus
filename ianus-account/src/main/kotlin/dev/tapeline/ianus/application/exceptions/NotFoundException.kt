package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class NotFoundException : IanusException("NOT_FOUND", "This entity does not exist")
