package com.jalgoarena

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import com.winterbe.expekt.should
import org.junit.Test


class ProblemsRepositorySpec {

    val repository = ProblemsRepository()

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
}
