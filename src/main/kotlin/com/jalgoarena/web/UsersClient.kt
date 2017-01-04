package com.jalgoarena.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.User
import com.netflix.discovery.EurekaClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class UsersClient(@Inject val discoveryClient: EurekaClient) {

    private fun authServiceUrl(): String {
        val instance = discoveryClient.getNextServerFromEureka("jalgoarena-auth", false)
        return instance.homePageUrl
    }

    private val httpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    fun findUser(token: String): User {
        val request = Request.Builder()
                .url("${authServiceUrl()}/api/user")
                .addHeader("X-Authorization", token)
                .build()

        val response = httpClient.newCall(request).execute()
        return objectMapper.readValue(response.body().string(), User::class.java)
    }
}
