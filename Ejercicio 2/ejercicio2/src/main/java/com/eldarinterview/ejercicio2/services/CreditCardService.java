package com.eldarinterview.ejercicio2.services;

import com.eldarinterview.ejercicio2.models.CreditCard;
import com.eldarinterview.ejercicio2.models.User;
import com.eldarinterview.ejercicio2.repositories.CreditCardRepository;
import com.eldarinterview.ejercicio2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con las tarjetas de crédito.
 */
@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Añade una nueva tarjeta de crédito y envía los detalles al correo electrónico del usuario.
     *
     * @param creditCard La tarjeta de crédito que se desea añadir.
     * @return La tarjeta de crédito guardada después de ser añadida a la base de datos.
     * @throws Exception si el usuario asociado no se encuentra.
     */
    public CreditCard addCreditCard(CreditCard creditCard) throws Exception {
        User user = userRepository.findById(creditCard.getUser().getId())
                .orElseThrow(() -> new Exception("User not found"));
        creditCard.setUser(user);
        CreditCard savedCard = creditCardRepository.save(creditCard);
        emailService.sendCreditCardDetails(user.getEmail(), savedCard);
        return savedCard;
    }

    /**
     * Obtiene una tarjeta de crédito por su ID.
     *
     * @param id El ID de la tarjeta de crédito que se desea obtener.
     * @return Un Optional que contiene la tarjeta de crédito encontrada, o vacío si no se encuentra.
     */
    public Optional<CreditCard> getCreditCardById(Long id) {
        return creditCardRepository.findById(id);
    }

    /**
     * Actualiza la información de una tarjeta de crédito existente.
     *
     * @param id El ID de la tarjeta de crédito que se desea actualizar.
     * @param card Un objeto CreditCard que contiene los nuevos datos de la tarjeta de crédito.
     * @return La tarjeta de crédito actualizada después de ser guardada en la base de datos.
     * @throws RuntimeException si la tarjeta de crédito no se encuentra.
     */
    public CreditCard updateCreditCard(Long id, CreditCard card) {
        CreditCard existingCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit card not found"));

        // Actualiza solo si el nuevo valor no es nulo
        if (card.getBrand() != null) {
            existingCard.setBrand(card.getBrand());
        }
        if (card.getCardNumber() != null) {
            existingCard.setCardNumber(card.getCardNumber());
        }
        if (card.getExpirationDate() != null) {
            existingCard.setExpirationDate(card.getExpirationDate());
        }
        if (card.getCardHolderName() != null) {
            existingCard.setCardHolderName(card.getCardHolderName());
        }
        if (card.getCvv() != null) {
            existingCard.setCvv(card.getCvv());
        }
        if (card.getUser() != null) {
            existingCard.setUser(card.getUser());
        }

        return creditCardRepository.save(existingCard);
    }

    /**
     * Elimina una tarjeta de crédito por su ID.
     *
     * @param id El ID de la tarjeta de crédito que se desea eliminar.
     * @throws RuntimeException si la tarjeta de crédito no se encuentra.
     */
    public void deleteCreditCard(Long id) {
        CreditCard existingCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit card not found"));
        creditCardRepository.delete(existingCard);
    }

    /**
     * Calcula la tarifa basada en la marca de la tarjeta y el monto proporcionado.
     *
     * @param brand  La marca de la tarjeta (VISA, NARA, AMEX).
     * @param amount El monto sobre el cual se calculará la tarifa.
     * @return La tarifa calculada basada en la marca de la tarjeta y el monto.
     * @throws IllegalArgumentException si la marca de la tarjeta es desconocida.
     */
    public double calculateFee(String brand, double amount) {
        LocalDate date = LocalDate.now();
        double feeRate = 0;
        switch (brand.toUpperCase()) {
            case "VISA":
                feeRate = (roundDecimals(((double) date.getYear() % 100) / date.getMonthValue())) / 100;
                break;
            case "NARA":
                feeRate = (roundDecimals(date.getDayOfMonth() * 0.5) / 100);
                break;
            case "AMEX":
                feeRate = (roundDecimals(date.getMonthValue() * 0.1) / 100);
                break;
            default:
                throw new IllegalArgumentException("Unknown card brand.");
        }

        return amount * feeRate;
    }

    /**
     * Redondea un valor decimal a cuatro lugares decimales.
     *
     * @param value El valor que se desea redondear.
     * @return El valor redondeado.
     */
    public static double roundDecimals(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP); // Redondea a 4 lugares decimales
        return bd.doubleValue();
    }
}
