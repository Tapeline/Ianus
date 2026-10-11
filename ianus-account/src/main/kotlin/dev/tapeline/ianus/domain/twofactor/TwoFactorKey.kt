package dev.tapeline.ianus.domain.twofactor


@JvmInline
value class TwoFactorKey(val value: String) {
    init {
        require(!value.isEmpty())
    }
}