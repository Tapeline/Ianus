package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.domain.IanusException

class InsecurePasswordException : IanusException("INSECURE_PASSWORD", "This password is insecure")