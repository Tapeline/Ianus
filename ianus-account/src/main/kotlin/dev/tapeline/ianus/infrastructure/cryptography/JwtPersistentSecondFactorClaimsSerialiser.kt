package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jwt.JWTClaimsSet
import dev.tapeline.ianus.domain.twofactor.SecondFactor
import dev.tapeline.ianus.domain.user.UserId
import java.time.Duration
import java.util.*

private const val USER_ID_CLAIM = "u"

class JwtPersistentSecondFactorClaimsSerialiser : ClaimsSerialiser<SecondFactor> {

    override fun serialiseClaims(claims: SecondFactor) =
        JWTClaimsSet.Builder()
            .issueTime(Date.from(claims.issuedAt))
            .expirationTime(Date.from(claims.expiresAt))
            .claim(USER_ID_CLAIM, claims.userId.value)
            .build()

    override fun deserialiseClaims(claims: JWTClaimsSet) =
        with(claims) {
            SecondFactor(
                userId = UserId(getStringClaim(USER_ID_CLAIM)),
                issuedAt = issueTime.toInstant(),
                ttl = Duration.ofMillis(expirationTime.time - issueTime.time)
            )
        }

}
