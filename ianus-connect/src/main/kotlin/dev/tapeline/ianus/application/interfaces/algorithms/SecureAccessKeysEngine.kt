package dev.tapeline.ianus.application.interfaces.algorithms

interface SecureAccessKeysEngine {

    fun generateAccessKey(): String
    fun generateRefreshKey(): String

}
