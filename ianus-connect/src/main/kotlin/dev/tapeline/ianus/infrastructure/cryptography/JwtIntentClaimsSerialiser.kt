package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jwt.JWTClaimsSet
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.intent.Intent
import dev.tapeline.ianus.domain.scope.Scope
import java.net.URI
import java.util.Date

private const val APP_ID_CLAIM = "a"
private const val SCOPES_CLAIM = "sc"
private const val INCLUDE_USER_INFO_CLAIM = "u"
private const val REDIRECT_URI_CLAIM = "r"
private const val STATE_CLAIM = "st"

class JwtIntentClaimsSerialiser : ClaimsSerialiser<Intent> {

    override fun serialiseClaims(claims: Intent) =
        JWTClaimsSet.Builder()
            .issueTime(Date.from(claims.issuedAt))
            .expirationTime(Date.from(claims.expiresAt))
            .claim(APP_ID_CLAIM, claims.appId.value)
            .claim(SCOPES_CLAIM, claims.scopes.joinToString(" ", transform = Scope::value))
            .claim(INCLUDE_USER_INFO_CLAIM, claims.includeUserInfo)
            .claim(REDIRECT_URI_CLAIM, claims.redirectUri.toString())
            .claim(STATE_CLAIM, claims.state)
            .build()

    override fun deserialiseClaims(claims: JWTClaimsSet) =
        with (claims) {
            Intent(
                AppId(claims.getStringClaim(APP_ID_CLAIM)),
                getStringClaim(SCOPES_CLAIM).split(" ").map(::Scope).toSet(),
                claims.getBooleanClaim(INCLUDE_USER_INFO_CLAIM),
                URI.create(claims.getStringClaim(REDIRECT_URI_CLAIM)),
                claims.getStringClaim(STATE_CLAIM),
                claims.issueTime.toInstant(),
                claims.expirationTime.toInstant(),
            )
        }

}
