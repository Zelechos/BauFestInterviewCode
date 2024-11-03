package com.eldarinterview.ejercicio2.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa un usuario en el sistema.
 *
 * Esta clase es una entidad JPA que se mapea a la tabla "users" en la base de datos.
 * Contiene información básica del usuario, como su nombre, DNI, correo electrónico y fecha de nacimiento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario.
     * Este campo se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha de nacimiento del usuario.
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Documento Nacional de Identidad (DNI) del usuario.
     * Este campo debe ser único y no puede ser nulo.
     */
    @Column(name = "dni", unique = true, nullable = false)
    private String dni;

    /**
     * Correo electrónico del usuario.
     * Este campo debe ser único y no puede ser nulo.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Nombre del usuario.
     * Este campo no puede ser nulo.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Apellido del usuario.
     * Este campo no puede ser nulo.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

}
