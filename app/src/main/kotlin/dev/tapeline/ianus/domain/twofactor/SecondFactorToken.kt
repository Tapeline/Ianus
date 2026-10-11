package dev.tapeline.ianus.domain.twofactor

@JvmInline
value class SecondFactorToken(val value: String) {
    init {
        require(!value.isEmpty())
    }
}

