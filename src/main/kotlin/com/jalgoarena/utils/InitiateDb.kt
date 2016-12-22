package com.jalgoarena.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.Constants
import com.jalgoarena.domain.Problem
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.File

fun main(args: Array<String>) {

    val file = File("problems.json")
    println("Starting to transfer db")
    println(file.absolutePath)

    val problems = jacksonObjectMapper().readValue(
            file, Array<Problem>::class.java
    )

    fun toJson(obj: Any): String {
        return jacksonObjectMapper().writeValueAsString(obj)
    }

    var count = 1
    val problemsStore = PersistentEntityStores.newInstance(Constants.problemsStorePath)

    problems.forEach { problem ->

        problemsStore.executeInTransaction { txn ->
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
        println("$count/54 is done")
        count++
    }
}
