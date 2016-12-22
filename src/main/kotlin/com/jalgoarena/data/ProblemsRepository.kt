package com.jalgoarena.data

import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores

class ProblemsRepository {

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

    private fun store() = PersistentEntityStores.newInstance(Constants.problemsStorePath)
}
