package dev.tapeline.ianus.infrastructure.storage

import dev.tapeline.ianus.application.exceptions.UserAlreadyExistsException
import dev.tapeline.ianus.application.interfaces.persistence.UserStorage
import dev.tapeline.ianus.domain.twofactor.TwoFactorConfig
import dev.tapeline.ianus.domain.user.Email
import dev.tapeline.ianus.domain.HashedPassword
import dev.tapeline.ianus.domain.user.User
import dev.tapeline.ianus.domain.user.UserId
import dev.tapeline.ianus.domain.user.Username
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.Duration

private val logger = KotlinLogging.logger {}

private const val TABLE_USERS = "ianus_users"
private const val COL_ID = "id"
private const val COL_USERNAME = "username"
private const val COL_PASSWORD = "password"
private const val COL_EMAIL = "email"
private const val COL_2FA = "tfa"
private const val COL_SESSION_TTL = "session_ttl"

@Component
class PostgresUserStorage(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : UserStorage {

    override fun getUser(id: UserId) =
        jdbcTemplate.query(
            """
                SELECT $COL_ID, $COL_USERNAME, $COL_PASSWORD, $COL_EMAIL, $COL_2FA, $COL_SESSION_TTL
                FROM $TABLE_USERS
                WHERE $COL_ID = :id;
            """.trimIndent(),
            mapOf("id" to id.value),
            userRowMapper
        ).firstOrNull()

    override fun saveUser(user: User) =
        jdbcTemplate.update(
            """
                UPDATE $TABLE_USERS SET
                $COL_EMAIL = :email,
                $COL_PASSWORD = :password,
                $COL_2FA = :tfa,
                $COL_SESSION_TTL = :sessionTtl
                WHERE $COL_ID = :id
            """.trimIndent(),
            mapOf(
                "id" to user.id.value,
                "email" to user.email?.value,
                "password" to user.password.value,
                "tfa" to user.twoFactor.name,
                "sessionTtl" to user.sessionTtl.toMillis()
            )
        ).let {}

    override fun createUser(user: User) =
        try {
            jdbcTemplate.update(
                """
                INSERT INTO $TABLE_USERS 
                ($COL_ID, $COL_USERNAME, $COL_PASSWORD, $COL_EMAIL, $COL_2FA, $COL_SESSION_TTL)
                VALUES (:id, :username, :password, :email, :tfa, :sessionTtl);
            """.trimIndent(),
                mapOf(
                    "id" to user.id.value,
                    "username" to user.username.value,
                    "email" to user.email?.value,
                    "password" to user.password.value,
                    "tfa" to user.twoFactor.name,
                    "sessionTtl" to user.sessionTtl.toMillis()
                )
            ).let {}
        } catch (e: DataIntegrityViolationException) {
            logger.warn(e) { "Couldn't create user" }
            throw UserAlreadyExistsException()
        }

    override fun getUser(username: Username) =
        jdbcTemplate.query(
            """
                SELECT $COL_ID, $COL_USERNAME, $COL_PASSWORD, $COL_EMAIL, $COL_2FA, $COL_SESSION_TTL
                FROM $TABLE_USERS
                WHERE $COL_USERNAME = :username;
            """.trimIndent(),
            mapOf("username" to username.value),
            userRowMapper
        ).firstOrNull()

}

val userRowMapper = RowMapper<User?> { rs, _ ->
    User(
        id = UserId(rs.getString(COL_ID)),
        username = Username(rs.getString(COL_USERNAME)),
        password = HashedPassword(rs.getString(COL_PASSWORD)),
        email = rs.getObjectOrNull<String>(COL_EMAIL)?.let(::Email),
        twoFactor = TwoFactorConfig.valueOf(rs.getString(COL_2FA)),
        sessionTtl = Duration.ofMillis(rs.getLong(COL_SESSION_TTL)),
    )
}