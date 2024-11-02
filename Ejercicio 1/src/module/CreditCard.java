package module;

import java.time.LocalDate;

/**
 * Represents a credit card with its details, including brand, card number,
 * cardholder name, expiration date, CVV, and the owner of the card.
 */
public class CreditCard {
    private String brand;                // The brand of the credit card (e.g., VISA, AMEX).
    private String cardNumber;           // The card number of the credit card.
    private String cardHolderName;       // The name of the cardholder.
    private LocalDate expirationDate;    // The expiration date of the credit card.
    private String cvv;                  // The CVV of the credit card (Sensitive data).
    private Person owner;                // The owner of the credit card.

    /**
     * Constructs a new CreditCard object with the specified details.
     *
     * @param brand The brand of the credit card.
     * @param cardNumber The card number of the credit card.
     * @param cardHolderName The name of the cardholder.
     * @param expirationDate The expiration date of the credit card.
     * @param cvv The CVV of the credit card.
     * @param owner The owner of the credit card.
     */
    public CreditCard(String brand, String cardNumber, String cardHolderName, LocalDate expirationDate, String cvv, Person owner) {
        this.brand = brand;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.owner = owner;
    }

    /**
     * Checks if the credit card is valid based on the expiration date.
     *
     * @return true if the credit card is valid (not expired), false otherwise.
     */
    public boolean isValid() {
        return expirationDate.isAfter(LocalDate.now());
    }

    /**
     * Returns the brand of the credit card.
     *
     * @return The brand of the credit card.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the credit card.
     *
     * @param brand The new brand of the credit card.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the card number of the credit card.
     *
     * @return The card number of the credit card.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the card number of the credit card.
     *
     * @param cardNumber The new card number of the credit card.
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Returns the name of the cardholder.
     *
     * @return The name of the cardholder.
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * Sets the name of the cardholder.
     *
     * @param cardHolderName The new name of the cardholder.
     */
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    /**
     * Returns the expiration date of the credit card.
     *
     * @return The expiration date of the credit card.
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the credit card.
     *
     * @param expirationDate The new expiration date of the credit card.
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Returns the CVV of the credit card.
     *
     * @return The CVV of the credit card.
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * Sets the CVV of the credit card.
     *
     * @param cvv The new CVV of the credit card.
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * Returns the owner of the credit card.
     *
     * @return The owner of the credit card.
     */
    public Person getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the credit card.
     *
     * @param owner The new owner of the credit card.
     */
    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
