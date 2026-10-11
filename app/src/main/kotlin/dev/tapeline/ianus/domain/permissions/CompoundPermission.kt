package dev.tapeline.ianus.domain.permissions

const val EQUAL = 0
const val STRONGER = 1
const val WEAKER = -1

@JvmInline
value class CompoundPermission(val value: List<String>) {

    init {
        require(value.isNotEmpty())
    }

    operator fun compareTo(requestedPermission: CompoundPermission) =
        (value + List(requestedPermission.value.size - value.size) { null })
            .zip(value + List(value.size - requestedPermission.value.size) { null })
            .asSequence()
            .map { (granted, requested) ->
                when {
                    granted == requested -> EQUAL
                    granted == "*" -> STRONGER
                    requested == "*" -> WEAKER
                    else -> WEAKER
                }
            }
            .dropWhile { it == EQUAL }
            .take(1)
            .toList()
            .takeIf(List<*>::isNotEmpty)
                ?.first()
                ?: 0

}

fun Permission.asCompound() =
    CompoundPermission(value.split("."))

fun CompoundPermission.asSimple() =
    Permission(value.joinToString("."))
