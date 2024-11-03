package com.eldarinterview.ejercicio2.services;

import com.eldarinterview.ejercicio2.models.User;
import com.eldarinterview.ejercicio2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el servicio de usuarios.
 * Esta clase verifica el correcto funcionamiento de las operaciones de registro de usuarios.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * Prueba unitaria para registrar un nuevo usuario.
     * Verifica que el usuario guardado no sea nulo y que se haya asignado un ID.
     */
    @Test
    public void testRegisterUser() {
        User user = new User(1L, LocalDate.of(1990, 1, 1), "644687", "john@example.com", "john", "Doi");
        User savedUser = userService.registerUser(user);
        assertNotNull(savedUser.getId());
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceMock;

    /**
     * Prueba unitaria para guardar un usuario utilizando un repositorio simulado.
     * Verifica que el usuario devuelto no sea nulo y que el nombre del usuario sea correcto.
     */
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setFirstName("Juan");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User response = userServiceMock.registerUser(user);

        assertNotNull(response);
        assertEquals("Juan", response.getFirstName());
    }
}
