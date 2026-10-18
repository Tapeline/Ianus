package dev.tapeline.ianus.domain.authcode

@JvmInline
value class AuthorisationCode(val value: String) {
    init {
        require(!value.isEmpty())
    }
}
