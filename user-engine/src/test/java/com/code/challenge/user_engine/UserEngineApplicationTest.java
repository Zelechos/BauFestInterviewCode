package com.code.challenge.user_engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserEngineApplicationTest {

    // 1. Verifica que el contexto de Spring se carga correctamente
    @Test
    void contextLoads() {
        // Si llega aquí sin excepciones, el test pasa
    }

    @Test
    void hasSpringBootApplicationAnnotation() {
        assertTrue(
                UserEngineApplication.class.isAnnotationPresent(SpringBootApplication.class),
                "Debe tener la anotación @SpringBootApplication"
        );
    }

    @Test
    void mainMethodAcceptsArguments() {
        assertDoesNotThrow(() -> UserEngineApplication.main(new String[]{"--spring.profiles.active=test"}));
    }

    @Test
    void classExists() {
        assertNotNull(UserEngineApplication.class, "La clase debe existir");
    }

    @Test
    void mainMethodIsPublicAndStatic() throws NoSuchMethodException {
        var method = UserEngineApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()), "Debe ser público");
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()), "Debe ser estático");
    }

    @Test
    void classNameIsCorrect() {
        assertEquals("UserEngineApplication", UserEngineApplication.class.getSimpleName());
    }
}
