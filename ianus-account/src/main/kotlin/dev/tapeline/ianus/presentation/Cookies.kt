package dev.tapeline.ianus.presentation

import org.springframework.http.ResponseCookie
import java.time.Duration

const val SESSION_TOKEN_COOKIE = "SESSIONID"
const val SECOND_FACTOR_VERIFICATION_COOKIE = "I2FA"
const val SECOND_FACTOR_PERSISTENCE_COOKIE = "SESSION2FA"

fun createSecureCookie(
	name: String, 
	value: String,
	lifetime: Duration,
) =
	ResponseCookie
		.from(name, value)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(lifetime)
        .sameSite("Lax")
        .build()
        .toString()
