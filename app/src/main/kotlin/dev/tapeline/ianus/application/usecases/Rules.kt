package dev.tapeline.ianus.application.usecases

import dev.tapeline.ianus.application.exceptions.InsecurePasswordException
import dev.tapeline.ianus.configuration.RegistrationConfiguration

fun RegistrationConfiguration.ensurePasswordValid(rawPassword: String) =
    if (rawPassword.length < minPasswordLength) {
        throw InsecurePasswordException()
    } else {
        Unit
    }
