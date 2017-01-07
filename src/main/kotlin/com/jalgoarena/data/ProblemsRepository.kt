package com.jalgoarena.data

import com.jalgoarena.domain.Problem

interface ProblemsRepository {
    fun findAll(): List<Problem>
    fun find(id: String): Problem?
    fun addOrUpdate(problem: Problem): Problem
    fun destroy()
}
