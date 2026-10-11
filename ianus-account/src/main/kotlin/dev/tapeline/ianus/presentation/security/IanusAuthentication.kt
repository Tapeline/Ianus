package dev.tapeline.ianus.presentation.security

import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class IanusAuthentication(
    val sessionToken: SessionToken?,
    val secondFactorToken: SecondFactorToken?,
    val twoFactorKey: TwoFactorKey?,
    private val authorities: Collection<GrantedAuthority> = emptyList()
) : Authentication {

    private var authenticated = true

    override fun getName(): String = sessionToken?.value ?: "anonymous"
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getCredentials(): Any? = sessionToken
    override fun getDetails(): Any? = null
    override fun getPrincipal(): Any? = sessionToken
    override fun isAuthenticated(): Boolean = authenticated
    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}
