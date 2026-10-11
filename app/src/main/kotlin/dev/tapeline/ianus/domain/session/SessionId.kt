package dev.tapeline.ianus.domain.session


@JvmInline
value class SessionId(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
