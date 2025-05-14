package com.code.challenge.user_engine.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

import com.code.challenge.user_engine.service.TokenBlacklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeAll;
import org.powermock.reflect.Whitebox;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    @Mock
    private TokenBlacklist tokenBlacklist;

    private static final SecretKey testSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private JwtTokenUtil jwtTokenUtil;
    private final Long EXPIRATION = 3600L;
    private UUID testUserId;

    @BeforeAll
    static void setUpClass() throws Exception {
        // Configuración del campo static final para testing
        Field field = JwtTokenUtil.class.getDeclaredField("secret");
        field.setAccessible(true);

        // Remover modificador final
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Establecer el valor
        field.set(null, testSecretKey);
    }

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        jwtTokenUtil = new JwtTokenUtil();
        Whitebox.setInternalState(jwtTokenUtil, "tokenBlacklist", tokenBlacklist);
        Whitebox.setInternalState(jwtTokenUtil, "expiration", EXPIRATION);
    }

    @Test
    void generateToken_shouldReturnValidJwtToken() {
        String token = jwtTokenUtil.generateToken(testUserId);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = Jwts.parser()
                .setSigningKey(testSecretKey)
                .parseClaimsJws(token)
                .getBody();

        assertEquals(testUserId.toString(), claims.getSubject());
    }

    @Test
    void generateToken_shouldContainExpirationDate() {
        String token = jwtTokenUtil.generateToken(testUserId);
        Claims claims = Jwts.parser()
                .setSigningKey(testSecretKey)
                .parseClaimsJws(token)
                .getBody();

        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void getUserIdFromToken_shouldReturnCorrectUserId() {
        String token = jwtTokenUtil.generateToken(testUserId);
        UUID userId = jwtTokenUtil.getUserIdFromToken(token);

        assertEquals(testUserId, userId);
    }

    @Test
    void getUserIdFromToken_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token";

        assertThrows(JwtException.class, () -> {
            jwtTokenUtil.getUserIdFromToken(invalidToken);
        });
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String token = jwtTokenUtil.generateToken(testUserId);
        when(tokenBlacklist.isBlacklisted(token)).thenReturn(false);

        assertTrue(jwtTokenUtil.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForBlacklistedToken() {
        String token = jwtTokenUtil.generateToken(testUserId);
        when(tokenBlacklist.isBlacklisted(token)).thenReturn(true);

        assertFalse(jwtTokenUtil.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() {
        // Crear token expirado
        String expiredToken = Jwts.builder()
                .setSubject(testUserId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(testSecretKey)
                .compact();

        when(tokenBlacklist.isBlacklisted(expiredToken)).thenReturn(false);

        assertFalse(jwtTokenUtil.validateToken(expiredToken));
    }

    @Test
    void validateToken_shouldReturnFalseForMalformedToken() {
        String malformedToken = "malformed.token";

        assertFalse(jwtTokenUtil.validateToken(malformedToken));
    }

    @Test
    void validateToken_shouldReturnFalseForEmptyToken() {
        assertFalse(jwtTokenUtil.validateToken(""));
    }

    @Test
    void validateToken_shouldReturnFalseForNullToken() {
        assertFalse(jwtTokenUtil.validateToken(null));
    }

    @Test
    void validateToken_shouldHandleSignatureException() {
        SecretKey differentKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String tokenWithDifferentKey = Jwts.builder()
                .setSubject(testUserId.toString())
                .signWith(differentKey)
                .compact();

        assertFalse(jwtTokenUtil.validateToken(tokenWithDifferentKey));
    }

    @Test
    void validateToken_shouldReturnFalseWhenTokenBlacklistThrowsException() {
        String token = "any.token";
        when(tokenBlacklist.isBlacklisted(token)).thenThrow(new RuntimeException("Blacklist error"));

        assertFalse(jwtTokenUtil.validateToken(token));
    }

    @Test
    void generateToken_shouldIncludeIssuedAtDate() {
        String token = jwtTokenUtil.generateToken(testUserId);
        Claims claims = Jwts.parser()
                .setSigningKey(testSecretKey)
                .parseClaimsJws(token)
                .getBody();

        assertNotNull(claims.getIssuedAt());
        assertTrue(claims.getIssuedAt().before(new Date()) ||
                claims.getIssuedAt().equals(new Date()));
    }

    @Test
    void getUserIdFromToken_shouldThrowExceptionForExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject(testUserId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(testSecretKey)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenUtil.getUserIdFromToken(expiredToken);
        });
    }

    @Test
    void validateToken_shouldReturnFalseForTokenWithInvalidSignature() {
        String validToken = jwtTokenUtil.generateToken(testUserId);
        // Modificar el token para invalidar la firma
        String invalidSignatureToken = validToken.substring(0, validToken.length() - 5) + "abcde";

        assertFalse(jwtTokenUtil.validateToken(invalidSignatureToken));
    }
}
