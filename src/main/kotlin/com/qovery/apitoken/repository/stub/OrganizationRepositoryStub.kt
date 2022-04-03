package com.qovery.apitoken.repository.stub

import com.qovery.apitoken.domain.Organization
import com.qovery.apitoken.repository.OrganizationRepository
import org.springframework.stereotype.Repository

@Repository
class OrganizationRepositoryStub : OrganizationRepository {
    private val organizationsById = mutableMapOf<Int, Organization>()

    override fun saveOrganization(organization: Organization) {
        organizationsById[organization.id] = organization
    }

    override fun getById(id: Int) = organizationsById.getValue(id)
}
