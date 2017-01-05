package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.Constants
import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import jetbrains.exodus.entitystore.PersistentStoreTransaction
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import javax.annotation.PreDestroy

@Repository
class ProblemsRepository(dbName: String) {

    constructor() : this(Constants.storePath)

    private val LOG = LoggerFactory.getLogger(this.javaClass)
    private val store: PersistentEntityStore = PersistentEntityStores.newInstance(dbName)

    fun findAll(): List<Problem> {
        return readonly {
            it.getAll(Constants.entityType).map { Problem.from(it) }
        }
    }

    fun find(id: String): Problem? {
        return readonly {
            it.find(
                    Constants.entityType,
                    Constants.problemId,
                    id
            ).map { Problem.from(it) }.firstOrNull()
        }
    }

    fun add(problem: Problem): Problem {
        return transactional {
            val entity = it.newEntity(Constants.entityType).apply {
                setProperty(Constants.problemId, problem.id)
                setProperty(Constants.problemTitle, problem.title)
                setProperty(Constants.problemDescription, problem.description)
                setProperty(Constants.problemLevel, problem.level)
                setProperty(Constants.problemMemoryLimit, problem.memoryLimit)
                setProperty(Constants.problemTimeLimit, problem.timeLimit)
                setProperty(Constants.problemFunction, toJson(problem.function!!))
                setProperty(Constants.problemTestCases, toJson(problem.testCases!!))
            }
            Problem.from(entity)
        }
    }

    @PreDestroy
    fun destroy() {
        var proceed = true
        var count = 1
        while (proceed && count <= 10) {
            try {
                LOG.info("trying to close persistent store. attempt {}", count)
                store.close()
                proceed = false
                LOG.info("persistent store closed")
            } catch (e: RuntimeException) {
                LOG.error("error closing persistent store", e)
                count++
            }
        }
    }

    private fun toJson(obj: Any): String =
            jacksonObjectMapper().writeValueAsString(obj)

    private fun <T> transactional(call: (PersistentStoreTransaction) -> T): T {
        return transactional(store, call)
    }

    private fun <T> readonly(call: (PersistentStoreTransaction) -> T): T {
        return readonly(store, call)
    }
}

fun <T> transactional(store: PersistentEntityStore, call: (PersistentStoreTransaction) -> T): T {
    return store.computeInTransaction { call(it as PersistentStoreTransaction) }
}

fun <T> readonly(store: PersistentEntityStore, call: (PersistentStoreTransaction) -> T): T {
    return store.computeInReadonlyTransaction { call(it as PersistentStoreTransaction) }
}
