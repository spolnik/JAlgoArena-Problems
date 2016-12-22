package com.jalgoarena.data

import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores

class ProblemsRepository {

    val problemsStore: PersistentEntityStore =
            PersistentEntityStores.newInstance(Constants.problemsStorePath)

    fun findAll(): List<Problem> = problemsStore.computeInReadonlyTransaction {
        it.getAll(Constants.problemEntityType).map { Problem.from(it) }
    }

    fun find(id: String): Problem? = problemsStore.computeInReadonlyTransaction { txn ->
        txn.find(
                Constants.problemEntityType,
                Constants.problemId,
                id
        ).map { Problem.from(it) }.firstOrNull()
    }
}
