package com.jalgoarena

import com.jalgoarena.data.Constants
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class ProblemsController {

    val repository = ProblemsRepository(Constants.problemsStorePath)

    @RequestMapping(path = arrayOf("/problems"), method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun problems(): List<Problem> = repository.findAll()


    @RequestMapping(path = arrayOf("/problems/{id}"), method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) = repository.find(id)

    @RequestMapping(path = arrayOf("/problems/new"), method = arrayOf(RequestMethod.POST), produces = arrayOf("application/json"))
    fun newProblem(@RequestBody problem: Problem) = repository.add(problem)
}
