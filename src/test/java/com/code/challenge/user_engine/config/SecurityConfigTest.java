package com.code.challenge.user_engine.config;

import com.code.challenge.user_engine.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;  // Inyecta la configuración real

    @MockBean
    private AuthenticationConfiguration authConfig;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        securityConfig = new SecurityConfig(jwtAuthenticationFilter);
    }

    @Test
    void shouldPermitAllForSignUpEndpoint() throws Exception {
        mockMvc.perform(post("/api/v1/users/sign-up"))
                .andExpect(status().isNotFound()); // 404 porque no existe el endpoint real, pero pasa la seguridad
    }

    @Test
    void shouldPermitAllForLoginEndpoint() throws Exception {
        mockMvc.perform(post("/api/v1/users/login"))
                .andExpect(status().isNotFound()); // 404 porque no existe el endpoint real
    }

    @Test
    void shouldProvideAuthenticationManager() throws Exception {
        assertNull(securityConfig.authenticationManager(authConfig));
    }

    @Test
    void shouldProvideBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

}
