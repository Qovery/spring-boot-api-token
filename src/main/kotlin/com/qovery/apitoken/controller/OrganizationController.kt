package com.qovery.apitoken.controller

import com.qovery.apitoken.domain.*
import com.qovery.apitoken.repository.ApiTokenRepository
import com.qovery.apitoken.repository.OrganizationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class OrganizationController(
    private val organizationRepository: OrganizationRepository,
    private val apiTokenRepository: ApiTokenRepository,
    @Value("\${api-token.crc32-secret-key}") private val crc32SecretKey: String
) {

    @GetMapping("/api/organization/{id}")
    fun getOrganizationById(@PathVariable id: Int): ResponseEntity<Organization> {
        val context = SecurityContextHolder.getContext()
        val authenticationToken = context.authentication as UsernamePasswordAuthenticationToken
        val user = authenticationToken.principal as User

        if (user.hasAccessToOrganization(id).not()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val organization = organizationRepository.getById(id)
        return ResponseEntity.ok(organization)
    }

    @PostMapping("/api/organization/{id}/apiToken")
    fun generateApiToken(@PathVariable id: Int): ResponseEntity<String> {
        val apiTokenValue = ApiToken.createToken(crc32SecretKey)
        val apiTokenValueHash = ApiToken.hashToken(apiTokenValue)

        val apiToken = ApiToken(apiTokenValueHash, "organization:$id")
        apiTokenRepository.save(apiToken)

        return ResponseEntity.status(HttpStatus.CREATED).body(apiTokenValue)
    }

    @DeleteMapping("/api/organization/{id}/apiToken")
    fun revokeApiToken(@PathVariable id: Int, @RequestBody body: RevokeApiTokenRequest): ResponseEntity<String> {
        apiTokenRepository.delete(body.apiToken)
        return ResponseEntity.noContent().build()
    }
}

data class RevokeApiTokenRequest(val apiToken: String)
