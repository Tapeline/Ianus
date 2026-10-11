package dev.tapeline.ianus.domain.permissions


@JvmInline
value class Permission(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
