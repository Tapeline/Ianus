package dev.tapeline.ianus.infrastructure.storage

import java.sql.ResultSet

inline fun <reified T> ResultSet.getObjectOrNull(columnLabel: String): T? {
    val obj = this.getObject(columnLabel)
    return if (this.wasNull()) null else obj as? T
}
