package dev.tapeline.ianus.domain.user


@JvmInline
value class UserId(val value: String) {
    init {
        require(!value.isEmpty())
    }
}

