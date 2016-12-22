package com.jalgoarena

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import com.jalgoarena.utils.SetupProblemsStore
import com.winterbe.expekt.should
import org.junit.*
import java.io.File


class ProblemsRepositorySpec {

    val repository: ProblemsRepository = ProblemsRepository("./ProblemsStoreForTests")

    @Before
    fun setUp() {
        SetupProblemsStore("./ProblemsStoreForTests").create()
    }

    @After
    fun tearDown() {
        SetupProblemsStore("./ProblemsStoreForTests").remove()
    }


    @Test
    fun should_return_all_available_problems() {
        val problems = repository.findAll()
        problems.should.have.size(54)
    }

    @Test
    fun should_return_particular_problem() {
        val problem: Problem = repository.find("fib")!!
        problem.title.should.equal("Fibonacci")
    }

    @Test
    fun should_allow_on_adding_new_problem() {
        val problems = jacksonObjectMapper().readValue(
                File("problems.json"), Array<Problem>::class.java
        )

        val fibProblem = problems.filter { it.id == "fib" }.first()

        val newTestProblem = Problem(
                "test-fib",
                "Test Title",
                fibProblem.description,
                fibProblem.timeLimit,
                fibProblem.memoryLimit,
                fibProblem.function,
                fibProblem.testCases,
                fibProblem.level
        )

        repository.add(newTestProblem)

        val savedProblem = repository.find("test-fib")
        savedProblem!!.title.should.equal("Test Title")
    }
}
