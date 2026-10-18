package dev.tapeline.ianus.domain.scope

@JvmInline
value class Scope(val value: String) {
    val isOIDC get() = value == "openid"

    companion object {
        val OIDC = Scope("openid")
    }
}
