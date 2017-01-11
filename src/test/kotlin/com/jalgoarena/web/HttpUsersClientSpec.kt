package com.jalgoarena.web

import com.jalgoarena.domain.User
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.EurekaClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations

class HttpUsersClientSpec {

    private val DUMMY_TOKEN = "Bearer sdaw9awdw"
    private val USER_ROLE = "USER"
    private val USERS_SERVICE_DUMMY_URL = "http://localhost:9999"

    private val discoveryClient = mock(EurekaClient::class.java)
    private val restTemplate = mock(RestOperations::class.java)
    private val usersClient = HttpUsersClient(discoveryClient, restTemplate)

    @Test
    fun delegates_get_user_request_to_auth_service_endpoint() {
        givenDiscoveryService()

        val headers = HttpHeaders().apply {
            set("X-Authorization", DUMMY_TOKEN)
        }

        val entity = HttpEntity<HttpHeaders>(headers)

        given(restTemplate.exchange(
                "$USERS_SERVICE_DUMMY_URL/api/user", HttpMethod.GET, entity, User::class.java
        )).willReturn(ResponseEntity.ok(User(USER_ROLE)))

        val user = usersClient.findUser(DUMMY_TOKEN)

        assertThat(user!!.role).isEqualTo(USER_ROLE)
    }

    @Test
    fun returns_null_if_there_is_any_issue_with_response() {
        givenDiscoveryService()

        val user = usersClient.findUser(DUMMY_TOKEN)
        assertThat(user).isNull()
    }

    private fun givenDiscoveryService() {
        val instanceInfo = mock(InstanceInfo::class.java)

        given(instanceInfo.homePageUrl).willReturn(USERS_SERVICE_DUMMY_URL)
        given(discoveryClient.getNextServerFromEureka("jalgoarena-auth", false)).willReturn(instanceInfo)
    }
}
