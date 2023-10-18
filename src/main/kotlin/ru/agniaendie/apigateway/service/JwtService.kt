package ru.agniaendie.apigateway.service

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
class JwtService {
    @Value("\${jwt.secret}")
    private val secret: String? = null
    //da
    fun getClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(Base64.encodeBase64String(secret!!.toByteArray())).parseClaimsJws(token).body
    }

    fun isExpired(token: String?): Boolean {
        return try {
            getClaims(token).expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}
