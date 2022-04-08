package com.qovery.apitoken.config

import com.qovery.apitoken.domain.ApiToken
import com.qovery.apitoken.domain.User
import com.qovery.apitoken.repository.ApiTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApiTokenRequestFilter(
    private val apiTokenRepository: ApiTokenRepository,
    @Value("\${api-token.crc32-secret-key}") private val crc32SecretKey: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Token ")) {
            val apiTokenValue = header.substring(6)
            if (ApiToken.isValid(apiTokenValue, crc32SecretKey)) {
                val apiToken = apiTokenRepository.get(ApiToken.hashToken(apiTokenValue))
                if (apiToken != null) {
                    val fakeUser = User("api-token", "", setOf(apiToken.role))
                    SecurityContextHolder.getContext().authentication =
                        UsernamePasswordAuthenticationToken(fakeUser, null, null)
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}
