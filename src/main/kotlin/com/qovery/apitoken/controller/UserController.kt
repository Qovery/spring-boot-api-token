package com.qovery.apitoken.controller

import com.qovery.apitoken.repository.UserRepository
import com.qovery.apitoken.service.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    @PostMapping("/api/user")
    fun authenticate(@RequestBody body: AuthenticateRequest): ResponseEntity<String> {
        val user = userRepository.findByUsername(body.username) ?: return ResponseEntity.notFound().build()

        if (bCryptPasswordEncoder.matches(body.password, user.encryptedPassword).not()) {
            return ResponseEntity.badRequest().build()
        }

        val jwt = jwtService.createJwt(user)
        return ResponseEntity.ok(jwt)
    }
}

data class AuthenticateRequest(
    val username: String,
    val password: String
)
