package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jwt.JWTClaimsSet
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.authcode.AuthorisationCodeClaims
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.domain.user.UserId
import java.util.*

private const val APP_ID_CLAIM = "a"
private const val SCOPES_CLAIM = "s"
private const val INCLUDE_USER_INFO_CLAIM = "u"
private const val USER_ID_CLAIM = "i"

class JwtAuthorisationCodeClaimsSerialiser : ClaimsSerialiser<AuthorisationCodeClaims> {

    override fun serialiseClaims(claims: AuthorisationCodeClaims) =
        JWTClaimsSet.Builder()
            .issueTime(Date.from(claims.issuedAt))
            .expirationTime(Date.from(claims.expiresAt))
            .claim(APP_ID_CLAIM, claims.appId.value)
            .claim(SCOPES_CLAIM, claims.scopes.joinToString(" ", transform = Scope::value))
            .claim(INCLUDE_USER_INFO_CLAIM, claims.includeUserInfo)
            .claim(USER_ID_CLAIM, claims.userId.value)
            .build()

    override fun deserialiseClaims(claims: JWTClaimsSet) =
        with (claims) {
            AuthorisationCodeClaims(
                AppId(claims.getStringClaim(APP_ID_CLAIM)),
                getStringClaim(SCOPES_CLAIM).split(" ").map(::Scope).toSet(),
                claims.getBooleanClaim(INCLUDE_USER_INFO_CLAIM),
                claims.getStringClaim(USER_ID_CLAIM).let(::UserId),
                claims.issueTime.toInstant(),
                claims.expirationTime.toInstant(),
            )
        }

}
