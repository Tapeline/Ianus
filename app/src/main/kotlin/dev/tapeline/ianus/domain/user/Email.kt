package dev.tapeline.ianus.domain.user


@JvmInline
value class Email(val value: String) {
    init {
        require(!value.isEmpty())
    }
}