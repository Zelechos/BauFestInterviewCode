package module;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing credit card operations such as registration,
 * retrieval, rate calculation, and filtering cards by person.
 */
public class CreditCardService {
    private Map<String, CreditCard> cards = new HashMap<>(); // Map to store registered credit cards by card number.

    /**
     * Registers a new credit card if it is valid.
     *
     * @param card The credit card to be registered.
     */
    public void registerCard(CreditCard card) {
        if (card.isValid()) {
            cards.put(card.getCardNumber(), card);
            System.out.println("Card registered successfully.");
        } else {
            System.out.println("Card expiration date is invalid.");
        }
    }

    /**
     * Retrieves a credit card by its card number.
     *
     * @param cardNumber The card number of the credit card to retrieve.
     * @return The CreditCard object associated with the given card number, or null if not found.
     */
    public CreditCard getCardByNumber(String cardNumber) {
        return cards.get(cardNumber);
    }

    /**
     * Calculates the rate for a given card brand and date.
     *
     * @param brand The brand of the credit card (e.g., VISA, NARA, AMEX).
     * @param date The date to be used in the rate calculation.
     * @return The calculated rate based on the brand and date.
     * @throws IllegalArgumentException If the brand is unknown.
     */
    public double calculateRate(String brand, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        switch (brand.toUpperCase()) {
            case "VISA":
                return roundDecimals(((double) date.getYear() % 100) / date.getMonthValue());
            case "NARA":
                return roundDecimals(date.getDayOfMonth() * 0.5);
            case "AMEX":
                return roundDecimals(date.getMonthValue() * 0.1);
            default:
                throw new IllegalArgumentException("Unknown card brand.");
        }
    }

    /**
     * Retrieves a list of credit cards owned by a specific person.
     *
     * @param person The person whose credit cards are to be retrieved.
     * @return A list of CreditCard objects owned by the specified person.
     */
    public List<CreditCard> getCardsByPerson(Person person) {
        return cards.values().stream()
                .filter(card -> card.getOwner().equals(person))
                .collect(Collectors.toList());
    }

    /**
     * Rounds a decimal value to four decimal places.
     *
     * @param value The value to be rounded.
     * @return The rounded value.
     */
    public static double roundDecimals(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP); // Rounds to 4 decimal places
        return bd.doubleValue();
    }
}
