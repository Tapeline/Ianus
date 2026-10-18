package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.common.IanusException

class CannotDeserialiseTokenException : IanusException("BAD_TOKEN", "Cannot deserialise token")
