package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.Constants
import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import jetbrains.exodus.entitystore.PersistentStoreTransaction
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class XodusProblemsRepository(dbName: String) : ProblemsRepository {

    constructor() : this(Constants.storePath)

    private val LOG = LoggerFactory.getLogger(this.javaClass)
    private val store: PersistentEntityStore = PersistentEntityStores.newInstance(dbName)

    override fun findAll(): List<Problem> {
        return readonly {
            it.getAll(Constants.entityType).map { Problem.from(it) }
        }
    }

    override fun find(id: String): Problem? {
        return readonly {
            it.find(
                    Constants.entityType,
                    Constants.problemId,
                    id
            ).map { Problem.from(it) }.firstOrNull()
        }
    }

    override fun addOrUpdate(problem: Problem): Problem {
        return transactional {

            val existingEntity = it.find(
                    Constants.entityType, Constants.problemId, problem.id
            ).firstOrNull()

            val entity = when (existingEntity) {
                null -> it.newEntity(Constants.entityType)
                else -> existingEntity
            }

            updateEntity(entity, problem)
        }
    }

    private fun updateEntity(entity: Entity, problem: Problem): Problem {
        entity.apply {
            setProperty(Constants.problemId, problem.id)
            setProperty(Constants.problemTitle, problem.title)
            setProperty(Constants.problemDescription, problem.description)
            setProperty(Constants.problemLevel, problem.level)
            setProperty(Constants.problemTimeLimit, problem.timeLimit)
            setProperty(Constants.problemFunction, toJson(problem.func!!))
            setProperty(Constants.problemTestCases, toJson(problem.testCases!!))
        }

        return Problem.from(entity)
    }

    override fun destroy() {
        try {
            LOG.info("Closing persistent store.")
            store.close()
            LOG.info("persistent store closed")
        } catch (e: RuntimeException) {
            LOG.error("error closing persistent store", e)
        }
    }

    private fun toJson(obj: Any): String =
            jacksonObjectMapper().writeValueAsString(obj)

    private fun <T> transactional(call: (PersistentStoreTransaction) -> T): T {
        return store.computeInTransaction { call(it as PersistentStoreTransaction) }
    }

    private fun <T> readonly(call: (PersistentStoreTransaction) -> T): T {
        return store.computeInReadonlyTransaction { call(it as PersistentStoreTransaction) }
    }
}
