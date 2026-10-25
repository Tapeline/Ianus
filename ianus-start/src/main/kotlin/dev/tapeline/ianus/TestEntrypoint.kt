package dev.tapeline.ianus

import dev.tapeline.ianus.infrastructure.cryptography.ArgonPasswordHashEngine

fun main() {
    println(ArgonPasswordHashEngine().hash("secret"))
}