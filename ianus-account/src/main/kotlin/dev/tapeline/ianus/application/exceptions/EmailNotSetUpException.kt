package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class EmailNotSetUpException : IanusException("EMAIL_NOT_SET_UP", "User email not set up")