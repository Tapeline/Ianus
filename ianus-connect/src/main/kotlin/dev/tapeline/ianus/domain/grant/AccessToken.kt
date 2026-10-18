package dev.tapeline.ianus.domain.grant

@JvmInline
value class AccessToken(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
