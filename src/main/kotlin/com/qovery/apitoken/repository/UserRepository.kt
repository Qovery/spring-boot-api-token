package com.qovery.apitoken.repository

import com.qovery.apitoken.domain.User

interface UserRepository {
    fun exists(username: String): Boolean
    fun saveUser(user: User)
    fun findByUsername(username: String): User?
}
