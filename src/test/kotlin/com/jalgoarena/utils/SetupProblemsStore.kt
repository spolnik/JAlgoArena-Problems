package com.jalgoarena.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.Constants
import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.File

class SetupProblemsStore(val dbName: String) {

    private fun toJson(obj: Any): String {
        return jacksonObjectMapper().writeValueAsString(obj)
    }

    fun create() {
        val store = PersistentEntityStores.newInstance(dbName)

        try {
            val problems = jacksonObjectMapper().readValue(
                    File("problems.json"), Array<Problem>::class.java
            )

            problems.forEach { problem ->

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
            }
        } finally {
            store.close()
        }
    }

    fun remove() {
        File(dbName).deleteRecursively()
    }
}
