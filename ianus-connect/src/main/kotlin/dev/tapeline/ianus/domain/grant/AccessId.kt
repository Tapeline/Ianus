package dev.tapeline.ianus.domain.grant

@JvmInline
value class AccessId(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
