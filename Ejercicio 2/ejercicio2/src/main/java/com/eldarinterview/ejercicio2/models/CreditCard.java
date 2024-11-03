package com.eldarinterview.ejercicio2.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa una tarjeta de crédito en el sistema.
 *
 * Esta entidad está mapeada a la tabla "credit_cards" en la base de datos.
 * Contiene información relevante sobre la tarjeta de crédito, incluyendo
 * la marca, número de tarjeta, fecha de expiración, nombre del titular
 * y el CVV. También establece una relación con la entidad User,
 * indicando a qué usuario pertenece la tarjeta.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "credit_cards") // Nombre de la tabla en la base de datos
public class CreditCard {

    /**
     * Identificador único de la tarjeta de crédito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Marca de la tarjeta de crédito (ejemplo: VISA, AMEX).
     * Este campo es obligatorio y no puede exceder 50 caracteres.
     */
    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    /**
     * Número de tarjeta de crédito, que debe ser único y tiene una longitud de 16 caracteres.
     * Este campo es obligatorio.
     */
    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;

    /**
     * Fecha de expiración de la tarjeta de crédito.
     * Este campo es obligatorio.
     */
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    /**
     * Nombre del titular de la tarjeta de crédito.
     * Este campo es obligatorio y no puede exceder 100 caracteres.
     */
    @Column(name = "card_holder_name", nullable = false, length = 100)
    private String cardHolderName;

    /**
     * Código de verificación de la tarjeta (CVV).
     * Este campo es obligatorio y debe contener exactamente 3 caracteres.
     */
    @Column(name = "cvv", nullable = false, length = 3) // CVV como cadena de 3 caracteres
    private String cvv;

    /**
     * Relación Many-to-One con la entidad User.
     * Indica el usuario al que pertenece esta tarjeta de crédito.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Relación con la tabla de usuarios
    private User user;
}
