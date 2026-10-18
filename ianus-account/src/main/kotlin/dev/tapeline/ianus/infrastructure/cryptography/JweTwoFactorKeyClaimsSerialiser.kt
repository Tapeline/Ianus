package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jwt.JWTClaimsSet
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorCode
import dev.tapeline.ianus.domain.twofactor.TwoFactorKeyClaims
import dev.tapeline.ianus.domain.user.UserId
import java.time.Duration
import java.util.Date

private const val USER_ID_CLAIM = "u"
private const val ORIGIN_CLAIM = "o"
private const val CODE_CLAIM = "c"

class JweTwoFactorKeyClaimsSerialiser : ClaimsSerialiser<TwoFactorKeyClaims> {

    override fun serialiseClaims(claims: TwoFactorKeyClaims) =
        JWTClaimsSet.Builder()
            .issueTime(Date.from(claims.issuedAt))
            .expirationTime(Date.from(claims.expiresAt))
            .claim(USER_ID_CLAIM, claims.userId.value)
            .claim(ORIGIN_CLAIM, claims.loginOrigin.value)
            .claim(CODE_CLAIM, claims.code.value)  // TODO: maybe replace with hash
            .build()

    override fun deserialiseClaims(claims: JWTClaimsSet) =
        with(claims) {
            TwoFactorKeyClaims(
                userId = UserId(getStringClaim(USER_ID_CLAIM)),
                loginOrigin = SessionOrigin(getStringClaim(ORIGIN_CLAIM)),
                issuedAt = issueTime.toInstant(),
                ttl = Duration.ofMillis(expirationTime.time - issueTime.time),
                code = TwoFactorCode(getStringClaim(CODE_CLAIM))
            )
        }

}
