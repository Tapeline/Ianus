package dev.tapeline.ianus.presentation.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap

fun <T : Any> toResponse(obj: T, status: HttpStatus) = ResponseEntity<T>(obj, status)
fun <T : Any> T.to200OK() = toResponse(this, HttpStatus.OK)
fun <T : Any> T.to201Created() = toResponse(this, HttpStatus.CREATED)
fun <T : Any> T.to204NoContent(): ResponseEntity<Void> = ResponseEntity.noContent().build<Void>()



