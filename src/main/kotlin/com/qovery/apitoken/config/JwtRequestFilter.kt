package com.qovery.apitoken.config

import com.qovery.apitoken.service.JwtService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal const val AUTHORIZATION_HEADER = "Authorization"

@Component
class JwtRequestFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(AUTHORIZATION_HEADER)
        if (header != null && header.startsWith("Bearer ")) {
            val jwt = header.substring(7)
            val user = jwtService.extractUserFromJwt(jwt)
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null, null)
        }
        filterChain.doFilter(request, response)
    }
}
