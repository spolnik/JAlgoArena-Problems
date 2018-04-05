package com.jalgoarena.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.Constants
import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.File

class SetupProblemsStore(private val dbName: String) {

    private fun toJson(obj: Any): String {
        return jacksonObjectMapper().writeValueAsString(obj)
    }

    fun createDb() {
        val persistentEntityStore = PersistentEntityStores.newInstance(dbName)

        persistentEntityStore.use { store ->
            val problems = jacksonObjectMapper().readValue(
                    File("problems.json"), Array<Problem>::class.java
            )

            problems.forEach { problem ->

                store.executeInTransaction { txn ->
                    txn.newEntity(Constants.ENTITY_TYPE).apply {
                        setProperty(Constants.problemId, problem.id)
                        setProperty(Constants.problemTitle, problem.title)
                        setProperty(Constants.problemDescription, problem.description)
                        setProperty(Constants.problemLevel, problem.level)
                        setProperty(Constants.problemTimeLimit, problem.timeLimit)
                        setProperty(Constants.problemFunction, toJson(problem.func!!))
                        setProperty(Constants.problemTestCases, toJson(problem.testCases!!))
                    }
                }
            }
        }
    }

    fun removeDb() {
        File(dbName).deleteRecursively()
    }
}

fun main(args: Array<String>) {
    val setup = SetupProblemsStore(Constants.STORE_PATH)
    setup.removeDb()
    setup.createDb()
}
