package dev.tapeline.ianus.domain.user


@JvmInline
value class HashedPassword(val value: String) {
    init {
        require(!value.isEmpty())
    }
}