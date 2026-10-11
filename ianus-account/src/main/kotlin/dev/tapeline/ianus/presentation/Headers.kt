package dev.tapeline.ianus.presentation

import org.springframework.http.HttpHeaders
import org.springframework.util.MultiValueMap

const val SET_COOKIE = "Set-Cookie"

fun headers(vararg headers: Pair<String, String>) =
    HttpHeaders(MultiValueMap.fromSingleValue<String, String>(mapOf(*headers)))

fun multiheaders(vararg headers: Pair<String, String>) =
    HttpHeaders(
        MultiValueMap.fromMultiValue<String, String>(
            headers.groupBy { it.first }.mapValues { it.value.map { it.second } }
        ))
