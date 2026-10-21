package dev.tapeline.ianus.infrastructure.storage

import dev.tapeline.ianus.application.interfaces.persistence.AccessGrantRepository
import dev.tapeline.ianus.application.interfaces.persistence.SessionStorage
import dev.tapeline.ianus.domain.session.Session
import dev.tapeline.ianus.domain.session.SessionId
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.session.SessionToken
import dev.tapeline.ianus.domain.user.UserId
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.sql.Timestamp

private val logger = KotlinLogging.logger {}

private const val TABLE_SESSIONS = "ianus_access_grants"
private const val COL_ID = "id"
private const val COL_TOKEN = "token"
private const val COL_USER_ID = "user_id"
private const val COL_ORIGIN = "origin"
private const val COL_CREATED_AT = "created_at"
private const val COL_TTL = "ttl"

@Component
class PostgresAccessGrantRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : AccessGrantRepository {

    override fun getSession(sessionId: SessionId) =
        jdbcTemplate.query(
            """
                SELECT $COL_ID, $COL_TOKEN, $COL_USER_ID, $COL_ORIGIN, $COL_CREATED_AT, $COL_TTL 
                FROM $TABLE_SESSIONS
                WHERE $COL_ID = :id;
            """.trimIndent(),
            mapOf("id" to sessionId.value),
            sessionRowMapper
        ).firstOrNull()

    override fun getSession(sessionToken: SessionToken) =
        jdbcTemplate.query(
            """
                SELECT $COL_ID, $COL_TOKEN, $COL_USER_ID, $COL_ORIGIN, $COL_CREATED_AT, $COL_TTL 
                FROM $TABLE_SESSIONS
                WHERE $COL_TOKEN = :token;
            """.trimIndent(),
            mapOf("token" to sessionToken.value),
            sessionRowMapper
        ).firstOrNull()

    override fun createSession(session: Session) =
        jdbcTemplate.update(
            """
                INSERT INTO $TABLE_SESSIONS ($COL_ID, $COL_TOKEN, $COL_USER_ID, $COL_ORIGIN, $COL_CREATED_AT, $COL_TTL)
                VALUES (:id, :token, :userId, :origin, :createdAt, :ttl);
            """.trimIndent(),
            mapOf(
                "id" to session.id.value,
                "token" to session.token.value,
                "userId" to session.userId.value,
                "origin" to session.origin.value,
                "createdAt" to Timestamp.from(session.createdAt),
                "ttl" to session.ttl.toMillis(),
            )
        ).let {
          logger.info { "Saved session " + session.token.value }
          logger.info { it.toString() }
        }

    override fun revokeSession(sessionId: SessionId) =
        jdbcTemplate.update(
            """
                DELETE FROM $TABLE_SESSIONS 
                WHERE $COL_ID = :id;
            """.trimIndent(),
            mapOf("id" to sessionId.value)
        ).let {}

    override fun getActiveSessions(userId: UserId) =
        jdbcTemplate.query(
            """
                SELECT $COL_ID, $COL_TOKEN, $COL_USER_ID, $COL_ORIGIN, $COL_CREATED_AT, $COL_TTL 
                FROM $TABLE_SESSIONS
                WHERE $COL_USER_ID = :userId;
            """.trimIndent(),
            mapOf("userId" to userId.value),
            sessionRowMapper
        ).filterNotNull()

}

val sessionRowMapper = RowMapper<Session?> { rs, _ ->
    Session(
        id = SessionId(rs.getString(COL_ID)),
        token = SessionToken(rs.getString(COL_TOKEN)),
        userId = UserId(rs.getString(COL_USER_ID)),
        origin = SessionOrigin(rs.getString(COL_ORIGIN)),
        createdAt = rs.getTimestamp(COL_CREATED_AT).toInstant(),
        ttl = Duration.ofMillis(rs.getLong(COL_TTL)),
    )
}