package dev.tapeline.ianus.infrastructure.cryptography

import dev.tapeline.ianus.common.newUuid
import java.security.SecureRandom
import kotlin.io.encoding.Base64

fun secureRandomBase64OfLength(lengthBytes: Int) =
    ByteArray(lengthBytes)
        .apply(SecureRandom()::nextBytes)
        .run(Base64::encode)
        .let { newUuid().toString() + "-" + it }
