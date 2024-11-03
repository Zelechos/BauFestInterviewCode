package com.eldarinterview.ejercicio2.services;

import com.eldarinterview.ejercicio2.models.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para enviar correos electrónicos relacionados con la tarjeta de crédito.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envía un correo electrónico con los detalles de la tarjeta de crédito al destinatario especificado.
     *
     * @param to La dirección de correo electrónico del destinatario.
     * @param creditCard El objeto CreditCard que contiene los detalles de la tarjeta de crédito a enviar.
     */
    public void sendCreditCardDetails(String to, CreditCard creditCard) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Credit Card Details");
        message.setText("Card Number: " + creditCard.getCardNumber() + "\nCVV: " + creditCard.getCvv());
        mailSender.send(message);
    }
}
