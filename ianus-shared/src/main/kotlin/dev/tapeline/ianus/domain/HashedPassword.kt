package dev.tapeline.ianus.domain

@JvmInline
value class HashedPassword(val value: String) {
    init {
        require(!value.isEmpty())
    }
}