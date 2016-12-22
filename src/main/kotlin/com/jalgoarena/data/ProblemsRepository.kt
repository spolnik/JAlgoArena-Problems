package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStores

class ProblemsRepository(val dbName: String) {

    fun findAll(): List<Problem> {
        val store = store()

        try {
            return store.computeInReadonlyTransaction {
                it.getAll(Constants.problemEntityType).map { Problem.from(it) }
            }
        } finally {
            store.close()
        }
    }

    fun find(id: String): Problem? {
        val store = store()

        try {
            return store.computeInReadonlyTransaction { txn ->
                txn.find(
                        Constants.problemEntityType,
                        Constants.problemId,
                        id
                ).map { Problem.from(it) }.firstOrNull()
            }
        } finally {
            store.close()
        }
    }

    fun add(problem: Problem) {
        val store = store()

        try {
            store.executeInTransaction { txn ->
                txn.newEntity(Constants.problemEntityType).apply {
                    setProperty(Constants.problemId, problem.id)
                    setProperty(Constants.problemTitle, problem.title)
                    setProperty(Constants.problemDescription, problem.description)
                    setProperty(Constants.problemLevel, problem.level)
                    setProperty(Constants.problemMemoryLimit, problem.memoryLimit)
                    setProperty(Constants.problemTimeLimit, problem.timeLimit)
                    setProperty(Constants.problemFunction, toJson(problem.function!!))
                    setProperty(Constants.problemTestCases, toJson(problem.testCases!!))
                }
            }
        } finally {
            store.close()
        }
    }

    private fun toJson(obj: Any): String =
            jacksonObjectMapper().writeValueAsString(obj)

    private fun store() =
            PersistentEntityStores.newInstance(dbName)
}
