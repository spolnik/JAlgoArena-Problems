package com.jalgoarena.web

import com.jalgoarena.domain.User

interface UsersClient {
    fun findUser(token: String): User
}
