package com.jalgoarena.web

import com.jalgoarena.domain.User
import com.netflix.discovery.EurekaClient
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import javax.inject.Inject

@Service
class HttpUsersClient(
        @Inject private val discoveryClient: EurekaClient,
        @Inject private val restTemplate : RestOperations
) : UsersClient {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    private fun authServiceUrl() =
            discoveryClient.getNextServerFromEureka("jalgoarena-auth", false).homePageUrl

    override fun findUser(token: String): User? {

        return try {
            val headers = HttpHeaders().apply {
                set("X-Authorization", token)
            }

            val entity = HttpEntity<HttpHeaders>(headers)

            val response = restTemplate.exchange(
                    "${authServiceUrl()}/api/user", HttpMethod.GET, entity, User::class.java)
            response.body
        } catch(e: Exception) {
            LOG.error("Error in querying jalgoarena auth service", e)
            null
        }
    }
}
