package com.jalgoarena.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.json.JacksonTester.initFields

class UserSerializationTest {

    private lateinit var json: JacksonTester<User>

    @Before
    fun setup() {
        val objectMapper = jacksonObjectMapper()
        initFields(this, objectMapper)
    }

    @Test
    fun should_serialize_user() {
        val user = User("USER")

        assertThat(json.write(user)).extractingJsonPathStringValue("@.role")
                .isEqualToIgnoringCase("USER")
    }

    @Test
    fun should_deserialize_user() {

        @Language("JSON")
        val userAsJson = "{ \"role\": \"ADMIN\" }"

        assertThat(json.parse(userAsJson)).isEqualTo(
                User("ADMIN")
        )
    }
}
