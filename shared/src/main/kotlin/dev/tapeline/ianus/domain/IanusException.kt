package dev.tapeline.ianus.domain

import java.lang.Exception

open class IanusException(
    val code: String,
    open val details: String
) : Exception("$code: $details")

open class InternalIanusException(details: String) :
    IanusException("INTERNAL", details)
