package com.eldarinterview.ejercicio2.repositories;

import com.eldarinterview.ejercicio2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz para la gestión de las operaciones de acceso a datos de los usuarios.
 *
 * Esta interfaz extiende JpaRepository, lo que proporciona una serie de métodos CRUD
 * predefinidos para la entidad User. También permite definir consultas personalizadas
 * basadas en la convención de nomenclatura.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su Documento Nacional de Identidad (DNI).
     *
     * @param dni El DNI del usuario que se busca.
     * @return Un objeto Optional que contiene el usuario encontrado, o vacío si no se encuentra ningún usuario con el DNI especificado.
     */
    Optional<User> findByDni(String dni);
}
