package com.jalgoarena.web

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@CrossOrigin
@RestController
class ProblemsController(
        @Inject val usersClient: UsersClient,
        @Inject val validation: UserPermissionValidation
) {

    @Autowired
    lateinit var repository: ProblemsRepository

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> = repository.findAll()

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) = repository.find(id)

    @PostMapping("/problems/new", produces = arrayOf("application/json"))
    fun newProblem(@RequestBody problem: Problem, @RequestHeader("X-Authorization") token: String) {
        val user = usersClient.findUser(token)
        validation.checkForAdmin(user)
        repository.add(problem)
    }
}
