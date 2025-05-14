package com.code.challenge.user_engine.security;

import com.code.challenge.user_engine.service.TokenBlacklist;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    private static final SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UUID userId) {
        return Jwts.builder().setSubject(userId.toString()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)).signWith(secret).compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        UUID uuidParsed = UUID.fromString(claims.getSubject());
        return UUID.fromString(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            if (tokenBlacklist.isBlacklisted(token)) {
                return false;
            }
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
