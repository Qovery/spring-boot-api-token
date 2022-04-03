package com.qovery.apitoken.controller

import com.qovery.apitoken.domain.Organization
import com.qovery.apitoken.domain.User
import com.qovery.apitoken.repository.OrganizationRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class OrganizationController(
    private val organizationRepository: OrganizationRepository
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
}
