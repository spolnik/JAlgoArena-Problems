package com.jalgoarena.web

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
class ProblemsController(
        @Inject private val repository: ProblemsRepository
) {

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> = repository.findAll()

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) = repository.find(id)
}
