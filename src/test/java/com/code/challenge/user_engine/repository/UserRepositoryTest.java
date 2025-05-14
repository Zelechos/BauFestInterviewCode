package com.code.challenge.user_engine.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.code.challenge.user_engine.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void testRepositoryIsNotNull() {
        assertNotNull(userRepository);
    }

    @Test
    void testFindByEmailWhenUserExists() {
        // Arrange
        String email = "test@example.com";
        User mockUser = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userRepository.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testFindByEmailWhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByEmail(email);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByEmailWithEmptyString() {
        // Arrange
        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByEmail("");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveUser() {
        // Arrange
        User newUser = User.builder()
                .email("new@user.com")
                .build();

        when(userRepository.save(newUser)).thenReturn(newUser);

        // Act
        User savedUser = userRepository.save(newUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals("new@user.com", savedUser.getEmail());
    }

    @Test
    void testFindByIdWhenUserExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User mockUser = User.builder()
                .id(userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userRepository.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    void testFindByIdWhenUserDoesNotExist() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findById(nonExistentId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userRepository.deleteById(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testFindAllUsers() {
        // Arrange
        User user1 = User.builder().email("user1@test.com").build();
        User user2 = User.builder().email("user2@test.com").build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertEquals(2, users.size());
    }

    @Test
    void testCountUsers() {
        // Arrange
        when(userRepository.count()).thenReturn(5L);

        // Act
        long count = userRepository.count();

        // Assert
        assertEquals(5L, count);
    }

    @Test
    void testExistsByEmailWhenUserExists() {
        // Arrange
        String email = "exists@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act
        boolean exists = userRepository.findByEmail(email).isPresent();

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByEmailWhenUserDoesNotExist() {
        // Arrange
        String email = "notexists@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean exists = userRepository.findByEmail(email).isPresent();

        // Assert
        assertFalse(exists);
    }

    @Test
    void testSaveAndFindUser() {
        // Arrange
        User user = User.builder()
                .email("test@save.com")
                .build();

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail("test@save.com")).thenReturn(Optional.of(user));

        // Act
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail("test@save.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testRepositoryAnnotation() {
        assertNotNull(UserRepository.class.getAnnotation(Repository.class));
    }

    @Test
    void testExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(UserRepository.class));
    }
}
