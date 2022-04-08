package com.qovery.apitoken.domain

import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import org.unbrokendome.base62.Base62
import java.security.SecureRandom
import java.util.zip.*

class ApiToken(
    val value: String,
    val role: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        fun createToken(crc32SecretKey: String): String {
            val secureRandom = SecureRandom()
            val randomString = (1..2).joinToString(separator = "") { Base62.encode(secureRandom.nextLong()) }
            val crc32 = CRC32().apply {
                update(crc32SecretKey.toByteArray())
                update(randomString.toByteArray())
            }.value
            return "prefix_${randomString}_$crc32"
        }

        fun isValid(token: String, crc32SecretKey: String): Boolean {
            val tokenDecodedSplit = token.split("_")

            if (tokenDecodedSplit.size != 3 || tokenDecodedSplit[0] != "prefix") {
                log.debug("API Token format error")
                return false
            }

            val randomString = tokenDecodedSplit[1]
            val crc32 = CRC32().apply {
                update(crc32SecretKey.toByteArray())
                update(randomString.toByteArray())
            }

            if (crc32.value.toString() != tokenDecodedSplit[2]) {
                log.debug("Inconsistent API Token")
                return false
            }
            return true
        }

        fun hashToken(apiTokenValue: String): String {
            return DigestUtils("SHA3-256").digestAsHex(apiTokenValue)
        }
    }
}
