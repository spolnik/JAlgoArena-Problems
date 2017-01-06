package com.jalgoarena.web

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@CrossOrigin
@RestController
class ProblemsController(
        @Inject val usersClient: UsersClient,
        @Inject val repository: ProblemsRepository
) {
    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> = repository.findAll()

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) = repository.find(id)

    @PostMapping("/problems/new", produces = arrayOf("application/json"))
    fun newProblem(
            @RequestBody problem: Problem,
            @RequestHeader("X-Authorization", required = false) token: String?
    ): ResponseEntity<Problem> {

        if (token == null) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val user = usersClient.findUser(token)

        return when {
            "ADMIN" == user.role -> ResponseEntity(
                    repository.addOrUpdate(problem), HttpStatus.CREATED
            )
            else -> ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
}
