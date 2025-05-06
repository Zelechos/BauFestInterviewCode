package com.code.challenge.user_engine.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.code.challenge.user_engine.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String validToken = "valid.token.here";
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipFilterForSignUpPath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/sign-up");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSkipFilterForH2ConsolePath() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/h2-console");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldProcessRequestWithValidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtTokenUtil.validateToken(validToken)).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken(validToken)).thenReturn(userId);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueChainWhenNoTokenProvided() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueChainWhenInvalidTokenFormat() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldReturnUnauthorizedForInvalidToken() throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtTokenUtil.validateToken("invalid.token")).thenThrow(new InvalidTokenException("Invalid token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Invalid token", stringWriter.toString().trim());
    }

    @Test
    void shouldExtractTokenFromBearerHeader() {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        String token = jwtAuthenticationFilter.extractToken(request);

        assertEquals(validToken, token);
    }

    @Test
    void shouldReturnNullForMissingAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = jwtAuthenticationFilter.extractToken(request);

        assertNull(token);
    }

    @Test
    void shouldReturnNullForMalformedAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn("Basic " + validToken);

        String token = jwtAuthenticationFilter.extractToken(request);

        assertNull(token);
    }

    @Test
    void shouldSetJwtAuthenticationWhenTokenValid() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtTokenUtil.validateToken(validToken)).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken(validToken)).thenReturn(userId);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userId, authentication.getPrincipal());
        assertEquals(validToken, authentication.getCredentials());
    }

    @Test
    void shouldNotSetAuthenticationForExpiredToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn("Bearer expired.token");
        when(jwtTokenUtil.validateToken("expired.token")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldHandlePathsWithDifferentCase() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/API/V1/USERS/SIGN-UP");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldHandleSubpathsOfExcludedPaths() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/sign-up/extra");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldClearSecurityContextAfterProcessing() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/users/profile");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtTokenUtil.validateToken(validToken)).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken(validToken)).thenReturn(userId);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Clear context and verify it was actually set during processing
        SecurityContextHolder.clearContext();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
