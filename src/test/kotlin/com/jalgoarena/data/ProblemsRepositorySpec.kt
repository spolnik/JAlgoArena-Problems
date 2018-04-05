package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.Problem
import com.jalgoarena.utils.SetupProblemsStore
import com.winterbe.expekt.should
import org.junit.AfterClass
import org.junit.Test
import java.io.File


class ProblemsRepositorySpec {

    companion object {
        private const val TEST_DB_NAME = "./ProblemsStoreForTests"

        var repository: ProblemsRepository
        init {
            SetupProblemsStore(TEST_DB_NAME).createDb()
            repository = XodusProblemsRepository(TEST_DB_NAME)
        }

        @AfterClass
        @JvmStatic fun tearDown() {
            repository.destroy()
            SetupProblemsStore(TEST_DB_NAME).removeDb()
        }
    }

    @Test
    fun should_return_all_available_problems() {
        val problems = repository.findAll()
        problems.should.have.size.above(54)
    }

    @Test
    fun should_return_particular_problem() {
        val problem: Problem = repository.find("fib")!!
        problem.title.should.equal("Fibonacci")
    }

    @Test
    fun should_return_all_test_cases_for_particular_problem() {
        val problem: Problem = repository.find("fib")!!
        problem.testCases!!.size.should.equal(9)
    }

    @Test
    fun should_allow_on_adding_new_problem() {
        val problems = jacksonObjectMapper().readValue(
                File("problems.json"), Array<Problem>::class.java
        )

        val fibProblem = problems.first { it.id == "fib" }

        val newTestProblem = Problem(
                "test-fib",
                "Test Title",
                fibProblem.description,
                fibProblem.timeLimit,
                fibProblem.func,
                fibProblem.testCases,
                fibProblem.level
        )

        repository.addOrUpdate(newTestProblem)

        val savedProblem = repository.find("test-fib")
        savedProblem!!.title.should.equal("Test Title")
    }
}
