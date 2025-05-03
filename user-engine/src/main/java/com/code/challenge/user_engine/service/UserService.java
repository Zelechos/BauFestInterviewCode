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

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phones(Optional.ofNullable(request.getPhones())
                        .orElse(List.of()).stream()
                        .map(phoneDto -> Phone.builder()
                                .id(UUID.randomUUID())
                                .number(phoneDto.getNumber())
                                .cityCode(phoneDto.getCityCode())
                                .countryCode(phoneDto.getCountryCode())
                                .build())
                        .collect(Collectors.toList()))
                .created(now)
                .lastLogin(now)
                .isActive(true)
                .build();

        user.setToken(jwtTokenUtil.generateToken(user.getId()));
        user = userRepository.save(user);

        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse login(String token) {
        System.out.printf("ESTE ES EL TOKEN ACTUAL => " + token);
        if (!jwtTokenUtil.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }

        UUID userId = jwtTokenUtil.getUserIdFromToken(token);
        System.out.println("USER ID QUE SE OBTIENE DEL TOKEN => " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        user.setToken(jwtTokenUtil.generateToken(user.getId()));
        user = userRepository.save(user);

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(user.getPhones().stream()
                        .map(phone -> PhoneDto.builder()
                                .number(phone.getNumber())
                                .cityCode(phone.getCityCode())
                                .countryCode(phone.getCountryCode())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
