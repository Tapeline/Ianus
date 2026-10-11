package dev.tapeline.ianus.domain.session


@JvmInline
value class SessionToken(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
