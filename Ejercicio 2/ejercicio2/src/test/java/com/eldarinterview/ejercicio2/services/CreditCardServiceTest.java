package com.eldarinterview.ejercicio2.services;

import com.eldarinterview.ejercicio2.repositories.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias para el servicio de tarjetas de crédito.
 * Esta clase verifica el cálculo de tarifas para diferentes marcas de tarjetas de crédito.
 */
@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardService creditCardService;

    /**
     * Prueba unitaria para calcular la tasa de una tarjeta VISA.
     * Verifica que el cálculo de la tarifa para una cantidad específica sea correcto.
     */
    @Test
    public void testCalculateVisaRate() {
        double rate = creditCardService.calculateFee("VISA", 10000);
        assertEquals(218.18, rate); // Example rate for testing
    }
}
