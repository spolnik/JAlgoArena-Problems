package com.jalgoarena.domain

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.json.JacksonTester

class ProblemSerializationTest {

    private lateinit var json: JacksonTester<Problem>

    @Before
    fun setup() {
        val objectMapper = jacksonObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }

    @Test
    fun should_serialize_problem() {
        val functionAsJson = json.write(TWO_SUM_PROBLEM)
        assertThat(functionAsJson).isEqualToJson("two-sum-problem.json")
    }

    @Test
    fun should_deserialize_problem() {
        assertThat(json.parse(TWO_SUM_PROBLEM_JSON))
                .isEqualTo(TWO_SUM_PROBLEM)
    }

    private val TWO_SUM_PROBLEM = Problem(
            "2-sum",
            "2 Sum",
            "Given an array of integers, find two numbers such that they add up to a specific target number.\r\n\r\nThe function `twoSum` should return indices of the two numbers such that they add up to the target, where *index1* must be less than *index2*. Please note that your returned answers (both *index1* and *index2*) are not zero-based.\r\n\r\n**Note**: You may assume that each input would have exactly one solution.\r\n\r\n### Example\r\n\r\n* `[2,7,11,15], 9` -> `[1,2]`",
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
