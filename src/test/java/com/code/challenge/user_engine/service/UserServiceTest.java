package com.code.challenge.user_engine.service;

import com.code.challenge.user_engine.dto.PhoneDto;
import com.code.challenge.user_engine.dto.SignUpRequest;
import com.code.challenge.user_engine.dto.UserResponse;
import com.code.challenge.user_engine.exception.InvalidTokenException;
import com.code.challenge.user_engine.exception.UserAlreadyExistsException;
import com.code.challenge.user_engine.exception.UserNotFoundException;
import com.code.challenge.user_engine.model.User;
import com.code.challenge.user_engine.repository.UserRepository;
import com.code.challenge.user_engine.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenUtil jwtTokenUtil;
    @Mock private TokenBlacklist tokenBlacklist;

    @InjectMocks
    private UserService userService;

    private SignUpRequest sampleRequest;

    @BeforeEach
    void setup() {
        sampleRequest = new SignUpRequest();
        sampleRequest.setEmail("test@example.com");
        sampleRequest.setName("Test User");
        sampleRequest.setPassword("password123");
        sampleRequest.setPhones(List.of());
    }

    @Test
    void signUp_shouldCreateUserSuccessfully() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtTokenUtil.generateToken(any())).thenReturn("jwt-token");

        UserResponse response = userService.signUp(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void signUp_shouldThrowWhenEmailAlreadyExists() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(mock(User.class)));

        assertThrows(UserAlreadyExistsException.class, () -> userService.signUp(sampleRequest));
    }

    @Test
    void login_shouldReturnUserResponse_whenTokenValid() {
        String token = "valid-token";
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("test@example.com").name("Test User").created(LocalDateTime.now()).lastLogin(LocalDateTime.now()).isActive(true).phones(List.of()).build();

        when(jwtTokenUtil.validateToken(token)).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(userId)).thenReturn("new-token");
        when(userRepository.save(any())).thenReturn(user);

        UserResponse response = userService.login(token);

        assertThat(response.getToken()).isEqualTo("new-token");
    }

    @Test
    void login_shouldThrowIfTokenIsNull() {
        assertThrows(InvalidTokenException.class, () -> userService.login(null));
    }

    @Test
    void login_shouldThrowIfTokenIsBlank() {
        assertThrows(InvalidTokenException.class, () -> userService.login("  "));
    }

    @Test
    void login_shouldThrowIfTokenInvalid() {
        when(jwtTokenUtil.validateToken("bad-token")).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> userService.login("bad-token"));
    }

    @Test
    void login_shouldThrowIfUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(jwtTokenUtil.validateToken("token")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login("token"));
    }

    @Test
    void login_shouldBlacklistOldToken() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("test@example.com").phones(List.of()).build();

        when(jwtTokenUtil.validateToken("token")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(userId)).thenReturn("new-token");
        when(userRepository.save(any())).thenReturn(user);

        userService.login("token");

        verify(tokenBlacklist).blacklistToken("token");
    }

    @Test
    void signUp_shouldEncodePassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtTokenUtil.generateToken(any())).thenReturn("token");

        userService.signUp(sampleRequest);

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void login_shouldUpdateLastLogin() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).phones(List.of()).build();

        when(jwtTokenUtil.validateToken("token")).thenReturn(true);
        when(jwtTokenUtil.getUserIdFromToken("token")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(userId)).thenReturn("new-token");
        when(userRepository.save(any())).thenReturn(user);

        UserResponse response = userService.login("token");

        assertThat(response.getLastLogin()).isNotNull();
    }

    @Test
    void login_shouldThrowOnExpiredJwtException() {
        when(jwtTokenUtil.validateToken("expired")).thenThrow(new ExpiredJwtException(null, null, "expired"));

        assertThrows(InvalidTokenException.class, () -> userService.login("expired"));
    }

    @Test
    void login_shouldThrowOnGenericJwtException() {
        when(jwtTokenUtil.validateToken("bad")).thenThrow(new JwtException("bad token"));

        assertThrows(InvalidTokenException.class, () -> userService.login("bad"));
    }

    @Test
    void signUp_shouldGenerateToken() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("enc");
        when(userRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtTokenUtil.generateToken(any())).thenReturn("generated");

        UserResponse response = userService.signUp(sampleRequest);

        assertThat(response.getToken()).isEqualTo("generated");
    }
}
