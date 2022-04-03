package com.qovery.apitoken.service

import com.qovery.apitoken.domain.User
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret-key}")
    secretKey: String
) {
    private val expirationTime = 60 * 10 * 1000
    private val secretKeyByteArray = secretKey.toByteArray()

    fun createJwt(user: User): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(user.username)
            .claim("roles", user.roles)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + expirationTime))
            .signWith(Keys.hmacShaKeyFor(secretKeyByteArray))
            .compact()
    }

    fun extractUserFromJwt(jwt: String): User {
        return extractClaims(jwt).let {
            User(
                username = it.subject,
                encryptedPassword = "",
                roles = extractRoles(it["roles"]!!)
            )
        }
    }

    private fun extractClaims(jwt: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKeyByteArray))
                .build()
                .parseClaimsJws(jwt)
                .body
        } catch (ex: Exception) {
            throw JwtParseException(ex)
        }
    }

    private fun extractRoles(roles: Any) = (roles as List<String>).toSet()
}

class JwtParseException(cause: Throwable) : RuntimeException(cause)
