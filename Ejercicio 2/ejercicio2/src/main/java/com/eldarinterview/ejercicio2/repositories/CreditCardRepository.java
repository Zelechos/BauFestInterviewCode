package com.eldarinterview.ejercicio2.repositories;

import com.eldarinterview.ejercicio2.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz para la gestión de las operaciones de acceso a datos de las tarjetas de crédito.
 *
 * Esta interfaz extiende JpaRepository, lo que proporciona una serie de métodos CRUD
 * predefinidos para la entidad CreditCard. Además, permite definir consultas personalizadas
 * basadas en la convención de nomenclatura.
 */
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    /**
     * Busca todas las tarjetas de crédito asociadas a un usuario específico.
     *
     * @param userId El identificador único del usuario cuyas tarjetas de crédito se buscan.
     * @return Una lista de tarjetas de crédito asociadas al usuario especificado.
     */
    List<CreditCard> findByUserId(Long userId);
}
