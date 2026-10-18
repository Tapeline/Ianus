package dev.tapeline.ianus.application.interfaces.algorithms

interface SecureKeyEngine {

    fun generateSessionSecret(): String

}
