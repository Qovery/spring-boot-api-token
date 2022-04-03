package com.qovery.apitoken

import com.qovery.apitoken.domain.Organization
import com.qovery.apitoken.domain.User
import com.qovery.apitoken.repository.OrganizationRepository
import com.qovery.apitoken.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@SpringBootApplication
class ApiTokenApplication

fun main(args: Array<String>) {
    runApplication<ApiTokenApplication>(*args)
}

@Component
class CommandLineStartup(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        createOrganizations()
        createUsers()
    }

    private fun createOrganizations() {
        sequenceOf(
            Organization(1, "organization_1"),
            Organization(2, "organization_2")
        ).forEach { organizationRepository.saveOrganization(it) }
    }

    private fun createUsers() {
        sequenceOf(
            User("user_1", bCryptPasswordEncoder.encode("password_1"), setOf("organization:1")),
            User("user_2", bCryptPasswordEncoder.encode("password_2"), setOf("organization:1", "organization:2"))
        ).forEach { userRepository.saveUser(it) }
    }
}
