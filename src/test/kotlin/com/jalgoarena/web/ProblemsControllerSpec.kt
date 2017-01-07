package com.jalgoarena.web

import com.fasterxml.jackson.databind.node.ArrayNode
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.User
import com.jalgoarena.utils.SetupProblemsStore
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.intellij.lang.annotations.Language
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject


@RunWith(SpringRunner::class)
@WebMvcTest(ProblemsController::class)
@ContextConfiguration(classes = arrayOf(ProblemsControllerSpec.ControllerTestConfiguration::class))
open class ProblemsControllerSpec {

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

    @MockBean
    private lateinit var usersClient: UsersClient

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
                .andExpect(jsonPath("$", hasSize<ArrayNode>(54)))
    }

    @Test
    fun returns_401_when_new_problem_is_added_without_authorization_token() {
        mockMvc.perform(put("/problems")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TWO_SUM_PROBLEM_JSON))
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun returns_401_when_new_problem_is_added_by_non_admin_user() {
        given(usersClient.findUser(DUMMY_TOKEN)).willReturn(User("USER"))

        mockMvc.perform(put("/problems")
                .header("X-Authorization", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TWO_SUM_PROBLEM_JSON))
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun returns_401_when_new_problem_is_added_by_unidentified_user() {
        given(usersClient.findUser(DUMMY_TOKEN)).willReturn(null)

        mockMvc.perform(put("/problems")
                .header("X-Authorization", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TWO_SUM_PROBLEM_JSON))
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun returns_201_and_newly_added_problem() {
        given(usersClient.findUser(DUMMY_TOKEN)).willReturn(User("ADMIN"))

        mockMvc.perform(put("/problems")
                .header("X-Authorization", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TWO_SUM_PROBLEM_JSON))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id", `is`("2-sum")))
    }

    @TestConfiguration
    open class ControllerTestConfiguration {
        @Bean
        open fun problemsRepository() = repository
    }

    private val DUMMY_TOKEN = "Bearer 123j12n31lkmdp012j21d"

    @Language("JSON")
    private val TWO_SUM_PROBLEM_JSON = """{
  "id": "2-sum",
  "title": "2 Sum",
  "description": "Given an array of integers, find two numbers such that they add up to a specific target number.\r\n\r\nThe function `twoSum` should return indices of the two numbers such that they add up to the target, where *index1* must be less than *index2*. Please note that your returned answers (both *index1* and *index2*) are not zero-based.\r\n\r\n**Note**: You may assume that each input would have exactly one solution.\r\n\r\n### Example\r\n\r\n* `[2,7,11,15], 9` -> `[1,2]`",
  "timeLimit": 1,
  "memoryLimit": 32,
  "function": {
    "name": "twoSum",
    "return": {
      "type": "[I",
      "comment": " Indices of the two numbers"
    },
    "parameters": [
      {
        "name": "nums",
        "type": "[I",
        "comment": "An array of Integer"
      },
      {
        "name": "target",
        "type": "java.lang.Integer",
        "comment": "target = numbers[index1] + numbers[index2]"
      }
    ]
  },
  "testCases": [
    {
      "input": [
        [
          2,
          7,
          11,
          15
        ],
        9
      ],
      "output": [
        1,
        2
      ]
    },
    {
      "input": [
        [
          1,
          0,
          -1
        ],
        -1
      ],
      "output": [
        2,
        3
      ]
    }
  ],
  "level": 2
}
"""
}
