package com.qovery.apitoken.repository.stub

import com.qovery.apitoken.domain.User
import com.qovery.apitoken.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryStub : UserRepository {
    private val usersByUsername = mutableMapOf<String, User>()

    override fun exists(username: String) = usersByUsername.containsKey(username)

    override fun saveUser(user: User) {
        usersByUsername.putIfAbsent(user.username, user)
    }

    override fun findByUsername(username: String) = usersByUsername[username]
}
