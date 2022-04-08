package com.qovery.apitoken.repository.stub

import com.qovery.apitoken.domain.ApiToken
import com.qovery.apitoken.repository.ApiTokenRepository
import org.springframework.stereotype.Repository

@Repository
class ApiTokenRepositoryStub : ApiTokenRepository {
    private val apiTokensByValue = mutableMapOf<String, ApiToken>()

    override fun save(apiToken: ApiToken) {
        apiTokensByValue[apiToken.value] = apiToken
    }

    override fun get(apiTokenValue: String): ApiToken? {
        return apiTokensByValue[apiTokenValue]
    }

    override fun delete(apiTokenValue: String) {
        apiTokensByValue.remove(apiTokenValue)
    }
}
