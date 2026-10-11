package dev.tapeline.ianus.presentation.security

import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.twofactor.SecondFactorToken
import dev.tapeline.ianus.domain.twofactor.TwoFactorKey
import dev.tapeline.ianus.presentation.SECOND_FACTOR_PERSISTENCE_COOKIE
import dev.tapeline.ianus.presentation.SECOND_FACTOR_VERIFICATION_COOKIE
import dev.tapeline.ianus.presentation.SESSION_TOKEN_COOKIE
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

@Order(1)
class IanusAuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val sessionTokenCookie = request.cookies?.firstOrNull { it.name == SESSION_TOKEN_COOKIE }
        val secondFactorCookie = request.cookies?.firstOrNull { it.name == SECOND_FACTOR_PERSISTENCE_COOKIE }
        val twoFactorKeyCookie = request.cookies?.firstOrNull { it.name == SECOND_FACTOR_VERIFICATION_COOKIE }

        val sessionToken = sessionTokenCookie?.value?.run(::SessionToken)
        val secondFactorToken = secondFactorCookie?.value?.run(::SecondFactorToken)
        val twoFactorKey = twoFactorKeyCookie?.value?.run(::TwoFactorKey)

        SecurityContextHolder.getContext().authentication = IanusAuthentication(
            sessionToken = sessionToken,
            secondFactorToken = secondFactorToken,
            twoFactorKey = twoFactorKey
        )

        filterChain.doFilter(request, response)
    }
}
