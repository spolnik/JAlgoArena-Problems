package com.jalgoarena.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.json.JacksonTester

class FunctionSerializationTest {

    private lateinit var json: JacksonTester<Function>

    @Before
    fun setup() {
        val objectMapper = jacksonObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }

    @Test
    fun should_serialize_function() {
        val functionAsJson = json.write(TWO_SUM_FUNCTION)
        assertThat(functionAsJson).isEqualToJson("two-sum-function.json")
    }

    @Test
    fun should_deserialize_function() {
        assertThat(json.parse(TWO_SUM_FUNCTION_AS_JSON))
                .isEqualTo(TWO_SUM_FUNCTION)
    }

    private val TWO_SUM_FUNCTION = Function("twoSum",
            Function.Return("[I",
                    " Indices of the two numbers"),
            listOf(Function.Parameter("numbers", "[I", "An array of Integer"),
                    Function.Parameter("target", "java.lang.Integer",
                            "target = numbers[index1] + numbers[index2]")
            )
    )

    @Language("JSON")
    private val TWO_SUM_FUNCTION_AS_JSON = """{
  "name": "twoSum",
  "returnStatement": {
    "type": "[I",
    "comment": " Indices of the two numbers"
  },
  "parameters": [
    {
      "name": "numbers",
      "type": "[I",
      "comment": "An array of Integer"
    },
    {
      "name": "target",
      "type": "java.lang.Integer",
      "comment": "target = numbers[index1] + numbers[index2]"
    }
  ]
}
"""
}
