package com.code.challenge.user_engine.service;

import com.code.challenge.user_engine.dto.PhoneDto;
import com.code.challenge.user_engine.dto.SignUpRequest;
import com.code.challenge.user_engine.dto.UserResponse;
import com.code.challenge.user_engine.exception.InvalidTokenException;
import com.code.challenge.user_engine.exception.UserAlreadyExistsException;
import com.code.challenge.user_engine.exception.UserNotFoundException;
import com.code.challenge.user_engine.model.Phone;
import com.code.challenge.user_engine.model.User;
import com.code.challenge.user_engine.repository.UserRepository;
import com.code.challenge.user_engine.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenBlacklist tokenBlacklist;

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        LocalDateTime now = LocalDateTime.now();
        User user = User.builder().name(request.getName()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).phones(Optional.ofNullable(request.getPhones()).orElse(List.of()).stream().map(phoneDto -> Phone.builder().number(phoneDto.getNumber()).cityCode(phoneDto.getCityCode()).countryCode(phoneDto.getCountryCode()).build()).collect(Collectors.toList())).created(now).lastLogin(now).isActive(true).build();
        user = userRepository.saveAndFlush(user);
        user.setToken(jwtTokenUtil.generateToken(user.getId()));

        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse login(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("Token is required");
        }

        try {
            if (!jwtTokenUtil.validateToken(token)) {
                throw new InvalidTokenException("Invalid or expired token");
            }

            UUID userId = jwtTokenUtil.getUserIdFromToken(token);
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

            user.setLastLogin(LocalDateTime.now());
            String newToken = jwtTokenUtil.generateToken(user.getId());
            user.setToken(newToken);
            tokenBlacklist.blacklistToken(token);
            user = userRepository.save(user);

            return mapToUserResponse(user);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expired");
        } catch (JwtException e) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder().id(user.getId()).created(user.getCreated()).lastLogin(user.getLastLogin()).token(user.getToken()).isActive(user.isActive()).name(user.getName()).email(user.getEmail()).password(user.getPassword()).phones(user.getPhones().stream().map(phone -> PhoneDto.builder().number(phone.getNumber()).cityCode(phone.getCityCode()).countryCode(phone.getCountryCode()).build()).collect(Collectors.toList())).build();
    }
}
