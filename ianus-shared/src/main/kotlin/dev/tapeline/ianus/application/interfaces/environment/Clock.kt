package dev.tapeline.ianus.application.interfaces.environment

import java.time.Instant

interface Clock {

    fun now(): Instant

}