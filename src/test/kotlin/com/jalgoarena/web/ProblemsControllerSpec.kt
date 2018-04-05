package com.jalgoarena.web

import com.fasterxml.jackson.databind.node.ArrayNode
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.data.XodusProblemsRepository
import com.jalgoarena.utils.SetupProblemsStore
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.hasSize
import org.junit.AfterClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebMvcTest(ProblemsController::class)
@ContextConfiguration(classes = [(ProblemsControllerSpec.ControllerTestConfiguration::class)])
open class ProblemsControllerSpec {

    companion object {
        private const val TEST_DB_NAME = "./ProblemsStoreForControllerTests"

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

    @Inject
    private lateinit var mockMvc: MockMvc

    @Test
    fun returns_200_and_queried_problem() {
        mockMvc.perform(get("/problems/fib")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`("fib")))
                .andExpect(jsonPath("$.title", `is`("Fibonacci")))
    }

    @Test
    fun returns_200_and_all_problems() {
        mockMvc.perform(get("/problems")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<ArrayNode>(greaterThan(54))))
    }

    @TestConfiguration
    open class ControllerTestConfiguration {
        @Bean
        open fun problemsRepository() = repository
    }
}
