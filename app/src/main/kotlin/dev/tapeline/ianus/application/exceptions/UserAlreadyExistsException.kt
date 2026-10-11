package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class UserAlreadyExistsException : IanusException("USER_ALREADY_EXISTS", "User already exists")