package com.qovery.apitoken.repository

import com.qovery.apitoken.domain.ApiToken

interface ApiTokenRepository {
    fun save(apiToken: ApiToken)
    fun get(apiTokenValue: String): ApiToken?
    fun delete(apiTokenValue: String)
}
