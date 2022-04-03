package com.qovery.apitoken.domain

class User(
    val username: String,
    val encryptedPassword: String,
    val roles: Set<String>
) {
    fun hasAccessToOrganization(id: Int) = roles.any { it == "organization:$id" }
}
