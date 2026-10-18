package dev.tapeline.ianus.infrastructure

import dev.tapeline.ianus.application.interfaces.persistence.Transaction
import dev.tapeline.ianus.domain.DomainEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.lang.reflect.UndeclaredThrowableException

@Component
class SpringTransaction(
    transactionManager: PlatformTransactionManager
) : Transaction {

    private val transactionTemplate = TransactionTemplate(transactionManager)

    override fun <R> invoke(block: Transaction.() -> R): R {
        return try {
            transactionTemplate.execute { block() } as R
        } catch (e: UndeclaredThrowableException) {
            throw e.undeclaredThrowable
        }
    }

    override fun publish(event: DomainEvent) {
        TODO("Not yet implemented")
    }

    override fun publishImmediately(event: DomainEvent) {
        TODO("Not yet implemented")
    }

}