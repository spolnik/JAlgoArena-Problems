package com.jalgoarena.data

import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores

class ProblemsRepository {

    val problemsStore: PersistentEntityStore =
            PersistentEntityStores.newInstance(Constants.problemsStorePath)

    fun findAll(): List<Problem> {

        var problems: List<Problem>? = null

        problemsStore.executeInReadonlyTransaction { txn ->
            val all = txn.getAll(Constants.problemEntityType)
            problems = all.map { Problem.from(it) }
        }

        return problems!!
    }

    fun find(id: String): Problem? {

        var problem: Problem? = null

        problemsStore.executeInReadonlyTransaction { txn ->
            val entity = txn.find(
                    Constants.problemEntityType,
                    Constants.problemId,
                    id
            )

            problem = Problem.from(entity.first())
        }

        return problem
    }
}
