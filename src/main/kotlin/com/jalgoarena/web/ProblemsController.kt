package com.jalgoarena.web

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import com.jalgoarena.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@CrossOrigin
@RestController
class ProblemsController(
        @Inject private val usersClient: UsersClient,
        @Inject private val repository: ProblemsRepository
) {

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> = repository.findAll()

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) = repository.find(id)

    @PutMapping("/problems", produces = arrayOf("application/json"))
    fun addOrUpdateProblem(
            @RequestBody problem: Problem,
            @RequestHeader("X-Authorization", required = false) token: String?
    ) = checkUser(token) { user ->
        when {
            "ADMIN" == user.role -> ResponseEntity(repository.addOrUpdate(problem), HttpStatus.CREATED)
            else -> unauthorized()
        }
    }

    private fun <T> checkUser(token: String?, action: (User) -> ResponseEntity<T>): ResponseEntity<T> {
        if (token == null) {
            return unauthorized()
        }

        val user = usersClient.findUser(token) ?: return unauthorized()
        return action(user)
    }

    private fun <T> unauthorized(): ResponseEntity<T> = ResponseEntity(HttpStatus.UNAUTHORIZED)
}
