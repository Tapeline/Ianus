package dev.tapeline.ianus.infrastructure.storage

import dev.tapeline.ianus.application.interfaces.persistence.AccessGrantRepository
import dev.tapeline.ianus.domain.app.AppId
import dev.tapeline.ianus.domain.grant.AccessGrant
import dev.tapeline.ianus.domain.grant.AccessId
import dev.tapeline.ianus.domain.grant.AccessRefreshToken
import dev.tapeline.ianus.domain.grant.AccessToken
import dev.tapeline.ianus.domain.scope.Scope
import dev.tapeline.ianus.domain.user.UserId
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.sql.Timestamp

private val logger = KotlinLogging.logger {}

private const val TABLE_ACCESS_GRANTS = "ianus_access_grants"
private const val COL_ID = "id"
private const val COL_TOKEN = "token"
private const val COL_APP_ID = "app_id"
private const val COL_SCOPES = "scopes"
private const val COL_USER_ID = "user_id"
private const val COL_ISSUED_AT = "issued_at"
private const val COL_TTL = "ttl"
private const val COL_REFRESH_TOKEN = "refresh_token"
private const val COL_REVOKED = "revoked"
private const val ALL_COLS = "$COL_ID, $COL_TOKEN, $COL_APP_ID, $COL_SCOPES, $COL_USER_ID, $COL_ISSUED_AT, $COL_TTL, $COL_REFRESH_TOKEN, $COL_REVOKED"

@Component
class PostgresAccessGrantRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : AccessGrantRepository {

    override fun getGrant(id: AccessId) =
        jdbcTemplate.query(
            """
                SELECT $ALL_COLS
                FROM $TABLE_ACCESS_GRANTS
                WHERE $COL_ID = :id;
            """.trimIndent(),
            mapOf("id" to id.value),
            accessGrantRowMapper
        ).firstOrNull()

    override fun getGrant(token: AccessToken) =
        jdbcTemplate.query(
            """
                SELECT $ALL_COLS
                FROM $TABLE_ACCESS_GRANTS
                WHERE $COL_TOKEN = :token;
            """.trimIndent(),
            mapOf("token" to token.value),
            accessGrantRowMapper
        ).firstOrNull()

    override fun createGrant(grant: AccessGrant) =
        jdbcTemplate.update(
            """
            INSERT INTO $TABLE_ACCESS_GRANTS 
            ($COL_ID, $COL_TOKEN, $COL_APP_ID, $COL_SCOPES, $COL_USER_ID, 
             $COL_ISSUED_AT, $COL_TTL, $COL_REFRESH_TOKEN, $COL_REVOKED)
            VALUES (:id, :token, :appId, :scopes, :userId, 
             :issuedAt, :ttl, :refreshToken, :revoked);
        """.trimIndent(),
            mapOf(
                "id" to grant.id.value,
                "token" to grant.token.value,
                "appId" to grant.appId.value,
                "scopes" to grant.scopes.joinToString(" ", transform = Scope::value),
                "userId" to grant.userId.value,
                "issuedAt" to Timestamp.from(grant.issuedAt),
                "ttl" to grant.ttl.toMillis(),
                "refreshToken" to grant.refreshToken.value,
                "revoked" to grant.revoked
            )
        ).let {}

    override fun updateGrant(grant: AccessGrant) =
        jdbcTemplate.update(
            """
            UPDATE $TABLE_ACCESS_GRANTS SET
            $COL_TOKEN = :token,
            $COL_ISSUED_AT = :issuedAt
            $COL_REVOKED = :revoked
            WHERE $COL_ID = :id;
        """.trimIndent(),
            mapOf(
                "id" to grant.id.value,
                "token" to grant.token.value,
                "issuedAt" to Timestamp.from(grant.issuedAt),
                "revoked" to grant.revoked
            )
        ).let {}

}

val accessGrantRowMapper = RowMapper<AccessGrant?> { rs, _ ->
    AccessGrant(
        id = AccessId(rs.getString(COL_ID)),
        token = AccessToken(rs.getString(COL_TOKEN)),
        appId = AppId(rs.getString(COL_APP_ID)),
        scopes = rs.getString(COL_SCOPES).split(" ").map(::Scope).toSet(),
        userId = rs.getString(COL_USER_ID).let(::UserId),
        issuedAt = rs.getTimestamp(COL_ISSUED_AT).toInstant(),
        ttl = Duration.ofMillis(rs.getLong(COL_TTL)),
        refreshToken = rs.getString(COL_REFRESH_TOKEN).let(::AccessRefreshToken),
        revoked = rs.getBoolean(COL_REVOKED)
    )
}
