package dev.tapeline.ianus.application.interfaces

import java.time.Instant

interface Clock {

    fun now(): Instant

}