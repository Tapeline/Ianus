package dev.tapeline.ianus.domain.twofactor


@JvmInline
value class TwoFactorCode(val value: String) {
    init {
        require(!value.isEmpty())
    }
}