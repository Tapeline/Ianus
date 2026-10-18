package dev.tapeline.ianus.domain.app

@JvmInline
value class AppName(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
