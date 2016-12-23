package com.jalgoarena

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class ProblemsController {

    @Autowired
    lateinit var repository: ProblemsRepository

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> = repository.findAll()

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) = repository.find(id)

    @PostMapping("/problems/new", produces = arrayOf("application/json"))
    fun newProblem(@RequestBody problem: Problem) = repository.add(problem)
}
