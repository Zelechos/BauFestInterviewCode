package com.code.challenge.user_engine.config;

import com.code.challenge.user_engine.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SecurityConfigTest {

//
//    @Autowired
//    private WebApplicationContext context;
//
//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    private MockMvc mockMvc;
//    private SecurityConfig securityConfig;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        securityConfig = new SecurityConfig(jwtAuthenticationFilter);
//    }
//
//    @Test
//    @DisplayName("1. Context Loads - Configuración de seguridad se carga correctamente")
//    void contextLoads() {
//        assertNotNull(securityConfig);
//    }
//
//    @Test
//    @DisplayName("2. CSRF Deshabilitado - Verifica que CSRF está deshabilitado")
//    void shouldDisableCsrf() throws Exception {
//        HttpSecurity http = mock(HttpSecurity.class);
//        securityConfig.securityFilterChain(http);
//        verify(http).csrf(any(Customizer.class));
//    }
//
//    @Test
//    @DisplayName("3. Endpoint SignUp Público - Permite acceso sin autenticación a /api/v1/users/sign-up")
//    void shouldPermitAllForSignUpEndpoint() throws Exception {
//        mockMvc.perform(post("/api/v1/users/sign-up"))
//                .andExpect(status().isNotFound()); // 404 porque no existe el endpoint real, pero pasa la seguridad
//    }
//
//    @Test
//    @DisplayName("4. Endpoint Login Público - Permite acceso sin autenticación a /api/v1/users/login")
//    void shouldPermitAllForLoginEndpoint() throws Exception {
//        mockMvc.perform(post("/api/v1/users/login"))
//                .andExpect(status().isNotFound()); // 404 porque no existe el endpoint real
//    }
//
//    @Test
//    @DisplayName("7. Sesión Stateless - Configura política de sesión sin estado")
//    void shouldConfigureStatelessSession() throws Exception {
//        HttpSecurity http = mock(HttpSecurity.class);
//        SessionManagementConfigurer<?> sessionConfig = mock(SessionManagementConfigurer.class);
//        when(http.sessionManagement()).thenReturn((SessionManagementConfigurer<HttpSecurity>) sessionConfig);
//
//        securityConfig.securityFilterChain(http);
//        verify(sessionConfig).sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//
//    @Test
//    @DisplayName("9. FrameOptions Deshabilitado - Para permitir H2 Console")
//    void shouldDisableFrameOptions() throws Exception {
//        HttpSecurity http = mock(HttpSecurity.class);
//        HeadersConfigurer<?> headersConfig = mock(HeadersConfigurer.class);
//        when(http.headers()).thenReturn((HeadersConfigurer<HttpSecurity>) headersConfig);
//
//        securityConfig.securityFilterChain(http);
//        verify(headersConfig).frameOptions().disable();
//    }
//
//    @Test
//    @DisplayName("10. AuthenticationManager Bean - Provee el bean correctamente")
//    void shouldProvideAuthenticationManager() throws Exception {
//        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
//        assertNotNull(securityConfig.authenticationManager(authConfig));
//    }
//
//    @Test
//    @DisplayName("11. PasswordEncoder Bean - Provee BCryptPasswordEncoder")
//    void shouldProvideBCryptPasswordEncoder() {
//        PasswordEncoder encoder = securityConfig.passwordEncoder();
//        assertTrue(encoder instanceof BCryptPasswordEncoder);
//    }

}
