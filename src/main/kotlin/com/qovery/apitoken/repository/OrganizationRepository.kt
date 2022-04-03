package com.qovery.apitoken.repository

import com.qovery.apitoken.domain.Organization

interface OrganizationRepository {
    fun saveOrganization(organization: Organization)
    fun getById(id: Int): Organization
}
