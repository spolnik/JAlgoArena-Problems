package com.jalgoarena.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.User
import com.netflix.discovery.EurekaClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class HttpUsersClient(@Inject val discoveryClient: EurekaClient) : UsersClient {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    private fun authServiceUrl(): String {
        val instance = discoveryClient.getNextServerFromEureka("jalgoarena-auth", false)
        return instance.homePageUrl
    }

    private val httpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override fun findUser(token: String): User? {
        val request = Request.Builder()
                .url("${authServiceUrl()}/api/user")
                .addHeader("X-Authorization", token)
                .build()

        return try {
            val response = httpClient.newCall(request).execute()
            objectMapper.readValue(response.body().string(), User::class.java)
        } catch(e: Exception) {
            LOG.error(e.message)
            null
        }
    }
}
