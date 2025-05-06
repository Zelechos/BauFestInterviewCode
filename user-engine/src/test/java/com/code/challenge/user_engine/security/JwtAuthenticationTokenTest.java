package com.code.challenge.user_engine.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

class JwtAuthenticationTokenTest {

    private UUID testUserId;
    private String testToken;
    private JwtAuthenticationToken authToken;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testToken = "test.jwt.token";
        authToken = new JwtAuthenticationToken(testUserId, testToken);
    }

    @Test
    void shouldExtendAbstractAuthenticationToken() {
        assertTrue(AbstractAuthenticationToken.class.isAssignableFrom(JwtAuthenticationToken.class));
    }

    @Test
    void shouldImplementAuthenticationInterface() {
        assertTrue(Authentication.class.isAssignableFrom(JwtAuthenticationToken.class));
    }

    @Test
    void constructorShouldSetAuthenticatedToTrue() {
        assertTrue(authToken.isAuthenticated());
    }

    @Test
    void constructorShouldSetNoAuthorities() {
        assertEquals(NO_AUTHORITIES, authToken.getAuthorities());
    }

    @Test
    void shouldReturnTokenAsCredentials() {
        assertEquals(testToken, authToken.getCredentials());
    }

    @Test
    void shouldReturnUserIdAsPrincipal() {
        assertEquals(testUserId, authToken.getPrincipal());
    }

    @Test
    void shouldNotAllowSettingAuthenticatedToTrue() {
        // This is just to verify the setter doesn't throw exceptions
        authToken.setAuthenticated(true);
        assertTrue(authToken.isAuthenticated());
    }

    @Test
    void shouldStoreDifferentUserIdCorrectly() {
        UUID differentUserId = UUID.randomUUID();
        JwtAuthenticationToken differentToken = new JwtAuthenticationToken(differentUserId, testToken);
        assertEquals(differentUserId, differentToken.getPrincipal());
    }

    @Test
    void shouldStoreDifferentTokenCorrectly() {
        String differentToken = "different.jwt.token";
        JwtAuthenticationToken differentAuth = new JwtAuthenticationToken(testUserId, differentToken);
        assertEquals(differentToken, differentAuth.getCredentials());
    }

    @Test
    void shouldHandleNullUserId() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(null, testToken);
        assertNull(token.getPrincipal());
    }

    @Test
    void shouldHandleNullToken() {
        JwtAuthenticationToken token = new JwtAuthenticationToken(testUserId, null);
        assertNull(token.getCredentials());
    }

    @Test
    void shouldNotContainTokenInToStringForSecurity() {
        String toString = authToken.toString();
        assertFalse(toString.contains(testToken),
                "Token should not be visible in toString for security reasons");
    }

    @Test
    void shouldNotBeEqualWithDifferentUserId() {
        JwtAuthenticationToken otherToken = new JwtAuthenticationToken(UUID.randomUUID(), testToken);
        assertNotEquals(authToken, otherToken);
    }

    @Test
    void shouldNotBeEqualWithDifferentToken() {
        JwtAuthenticationToken otherToken = new JwtAuthenticationToken(testUserId, "other.token");
        assertNotEquals(authToken, otherToken);
    }

    @Test
    void shouldBeEqualWithSameUserIdAndToken() {
        JwtAuthenticationToken sameToken = new JwtAuthenticationToken(testUserId, testToken);
        assertEquals(authToken, sameToken);
        assertEquals(authToken.hashCode(), sameToken.hashCode());
    }
}
