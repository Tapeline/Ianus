package dev.tapeline.ianus.domain.grant

@JvmInline
value class AccessRefreshToken(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
