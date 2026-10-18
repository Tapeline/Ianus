package dev.tapeline.ianus.application.interfaces.persistence

import dev.tapeline.ianus.domain.DomainEvent

interface Transaction {

    operator fun <R> invoke(block: Transaction.() -> R): R
    fun publish(event: DomainEvent)
    fun publishImmediately(event: DomainEvent)

}