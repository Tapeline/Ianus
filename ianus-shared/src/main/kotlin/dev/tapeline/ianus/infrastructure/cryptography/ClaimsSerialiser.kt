package dev.tapeline.ianus.infrastructure.cryptography

import com.nimbusds.jwt.JWTClaimsSet

interface ClaimsSerialiser<T> {

    fun serialiseClaims(claims: T): JWTClaimsSet
    fun deserialiseClaims(claims: JWTClaimsSet): T

}
