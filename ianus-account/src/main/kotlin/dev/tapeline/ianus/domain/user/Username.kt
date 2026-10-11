package dev.tapeline.ianus.domain.user


@JvmInline
value class Username(val value: String) {
    init {
        require(!value.isEmpty())
    }
}