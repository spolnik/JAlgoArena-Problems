package com.jalgoarena.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import com.jalgoarena.domain.User
import com.jalgoarena.utils.SetupProblemsStore
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.AfterClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject


@RunWith(SpringRunner::class)
@WebMvcTest(ProblemsController::class)
@ContextConfiguration(classes = arrayOf(ProblemsControllerTest.ProblemsControllerTestConfiguration::class))
open class ProblemsControllerTest {

    companion object {
        val dbName = "./ProblemsStoreForControllerTests"
        var repository: ProblemsRepository

        init {
            SetupProblemsStore(dbName).createDb()
            repository = ProblemsRepository(dbName)
        }

        @AfterClass
        @JvmStatic fun tearDown() {
            repository.destroy()
            SetupProblemsStore(dbName).removeDb()
        }
    }

    @Inject
    private lateinit var mockMvc: MockMvc

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var usersClient: UsersClient

    @Test
    fun returns_fib_problem_successfully() {
        mockMvc.perform(get("/problems/fib")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`("fib")))
                .andExpect(jsonPath("$.title", `is`("Fibonacci")))
    }

    @Test
    fun returns_all_problems() {
        mockMvc.perform(get("/problems")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<ArrayNode>(54)))
    }

    @Test
    fun adding_new_problem_without_authorization_token_results_in_401() {
        mockMvc.perform(post("/problems/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TWO_SUM_PROBLEM)))
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun adding_new_problem_by_user_results_in_401() {
        val dummyToken = "Bearer 123j12n31lkmdp012j21d"
        given(usersClient.findUser(dummyToken)).willReturn(User("USER"))

        mockMvc.perform(post("/problems/new")
                .header("X-Authorization", dummyToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TWO_SUM_PROBLEM)))
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun adds_new_problem() {
        val dummyToken = "Bearer 123j12n31lkmdp012j21d"
        given(usersClient.findUser(dummyToken)).willReturn(User("ADMIN"))

        mockMvc.perform(post("/problems/new")
                .header("X-Authorization", dummyToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TWO_SUM_PROBLEM)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id", `is`("2-sum")))
    }

    @TestConfiguration
    open class ProblemsControllerTestConfiguration {
        @Bean
        open fun problemsRepository() = ProblemsControllerTest.repository
    }

    private val TWO_SUM_PROBLEM = Problem(
            "2-sum",
            "2 Sum",
            "Given an array of integers, find two numbers such that they addOrUpdate up to a specific target number.\r\n\r\nThe function `twoSum` should return indices of the two numbers such that they addOrUpdate up to the target, where *index1* must be less than *index2*. Please note that your returned answers (both *index1* and *index2*) are not zero-based.\r\n\r\n**Note**: You may assume that each input would have exactly one solution.\r\n\r\n### Example\r\n\r\n* `[2,7,11,15], 9` -> `[1,2]`",
            1L,
            32,
            Function("twoSum",
                    Function.Return("[I",
                            " Indices of the two numbers"),
                    listOf(Function.Parameter("nums", "[I", "An array of Integer"),
                            Function.Parameter("target", "java.lang.Integer",
                                    "target = numbers[index1] + numbers[index2]")
                    )
            ),
            listOf(
                    Problem.TestCase(
                            arrayNode().add(
                                    arrayNode().add(2).add(7).add(11).add(15)
                            ).add(IntNode(9)),
                            arrayNode().add(1).add(2)
                    ),
                    Problem.TestCase(
                            arrayNode().add(
                                    arrayNode().add(1).add(0).add(-1)
                            ).add(IntNode(-1)),
                            arrayNode().add(2).add(3)
                    )
            ),
            2
    )

    private fun arrayNode() = ArrayNode(JsonNodeFactory.instance)
}
