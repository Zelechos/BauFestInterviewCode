package com.eldarinterview.ejercicio2.controllers;

import com.eldarinterview.ejercicio2.models.CreditCard;
import com.eldarinterview.ejercicio2.models.User;
import com.eldarinterview.ejercicio2.repositories.UserRepository;
import com.eldarinterview.ejercicio2.services.CreditCardService;
import com.eldarinterview.ejercicio2.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las tarjetas de crédito.
 */
@RestController
@RequestMapping("/api/cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Crea una nueva tarjeta de crédito.
     *
     * @param card La tarjeta de crédito que se desea crear.
     * @return Un ResponseEntity que contiene la tarjeta de crédito creada y el estado HTTP correspondiente.
     */
    @PostMapping("/alta")
    public ResponseEntity<CreditCard> altaTarjeta(@RequestBody CreditCard card) {
        try {
            // Validaciones para asegurarse de que todos los campos necesarios están presentes
            if (card.getBrand() == null || card.getCardNumber() == null ||
                    card.getExpirationDate() == null || card.getCardHolderName() == null ||
                    card.getCvv() == null || card.getUser() == null || card.getUser().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            CreditCard savedCard = creditCardService.addCreditCard(card);
            User user = userRepository.findById(savedCard.getUser().getId())
                    .orElseThrow(() -> new Exception("User not found"));
            // Enviar CVV al email del usuario
            emailService.sendCreditCardDetails(user.getEmail(), savedCard);
            return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza los datos de una tarjeta de crédito existente.
     *
     * @param id   El ID de la tarjeta de crédito que se desea actualizar.
     * @param card Un objeto CreditCard que contiene los nuevos datos de la tarjeta.
     * @return Un ResponseEntity que contiene la tarjeta de crédito actualizada y el estado HTTP correspondiente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> updateCard(@PathVariable Long id, @RequestBody CreditCard card) {
        try {
            CreditCard updatedCard = creditCardService.updateCreditCard(id, card);
            return new ResponseEntity<>(updatedCard, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una tarjeta de crédito por su ID.
     *
     * @param id El ID de la tarjeta de crédito que se desea eliminar.
     * @return Un ResponseEntity con el estado HTTP correspondiente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        try {
            creditCardService.deleteCreditCard(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene una tarjeta de crédito por su ID.
     *
     * @param id El ID de la tarjeta de crédito que se desea obtener.
     * @return Un ResponseEntity que contiene la tarjeta de crédito solicitada y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCardById(@PathVariable Long id) {
        try {
            Optional<CreditCard> card = creditCardService.getCreditCardById(id);
            return new ResponseEntity<>(card.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Calcula la tarifa basada en la marca de la tarjeta y el monto proporcionado.
     *
     * @param brand  La marca de la tarjeta (VISA, NARA, AMEX).
     * @param amount El monto sobre el cual se calculará la tarifa.
     * @return La tarifa calculada.
     */
    @GetMapping("/fee/{brand}/{amount}")
    public double getFee(@PathVariable String brand, @PathVariable Long amount) {
        return creditCardService.calculateFee(brand, amount);
    }
}
