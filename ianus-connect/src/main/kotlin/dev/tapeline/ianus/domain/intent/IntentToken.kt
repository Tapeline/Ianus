package dev.tapeline.ianus.domain.intent

@JvmInline
value class IntentToken(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
