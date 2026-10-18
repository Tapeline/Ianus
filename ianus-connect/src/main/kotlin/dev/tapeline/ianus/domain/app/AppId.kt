package dev.tapeline.ianus.domain.app

@JvmInline
value class AppId(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
