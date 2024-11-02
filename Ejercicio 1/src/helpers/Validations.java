package helpers;

/**
 * A utility class for validating various inputs such as emails, credit card details, and personal information.
 */
public class Validations {

    /**
     * Validates if the given email address is in a proper format.
     * @param email The email address to validate.
     * @return true if the email contains '@' and '.', false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    /**
     * Validates if the given brand is one of the accepted card brands.
     * @param brand The brand name to validate.
     * @return true if the brand is either "VISA", "NARA", or "AMEX", false otherwise.
     */
    public static boolean isValidBrand(String brand) {
        return brand.equals("VISA") || brand.equals("NARA") || brand.equals("AMEX");
    }

    /**
     * Validates if the given credit card number is valid.
     * The card number must consist of exactly 16 digits.
     * @param cardNumber The credit card number to validate.
     * @return true if the card number is valid, false otherwise.
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (!cardNumber.matches("\\d{16}")) {
            System.out.println("The card number must contain only digits and be 16 digits long.");
            return false;
        }
        return true;
    }

    /**
     * Validates if the given CVV (Card Verification Value) is valid.
     * The CVV must consist of exactly 3 digits.
     * @param cvv The CVV to validate.
     * @return true if the CVV is valid, false otherwise.
     */
    public static boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3}"); // CVV should be 3 digits
    }

    /**
     * Validates if the given DNI (Documento Nacional de Identidad) is valid.
     * The DNI must consist of 7 or 8 digits.
     * @param dni The DNI to validate.
     * @return true if the DNI is valid, false otherwise.
     */
    public static boolean isValidDni(String dni) {
        if (dni == null || dni.isEmpty()) {
            System.out.println("DNI cannot be null or empty.");
            return false;
        }
        if (!dni.matches("\\d{7,8}")) {
            System.out.println("DNI must contain only digits and be between 7 and 8 digits long.");
            return false;
        }
        return true;
    }

    /**
     * Validates if the given name is valid.
     * The name must contain only letters and spaces and cannot be null or empty.
     * @param name The name to validate.
     * @return true if the name is valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        String regex = "^[A-Za-zÀ-ÿ ]+$";
        if (name == null || name.isEmpty()) {
            System.out.println("The name cannot be null or empty.");
            return false;
        }

        if (!name.matches(regex)) {
            System.out.println("The name can only contain letters and spaces.");
            return false;
        }

        return true;
    }

    /**
     * Validates if the given last name is valid.
     * The last name must contain only letters and spaces and cannot be null or empty.
     * @param lastName The last name to validate.
     * @return true if the last name is valid, false otherwise.
     */
    public static boolean isValidLastName(String lastName) {
        String regex = "^[A-Za-zÀ-ÿ ]+$";
        if (lastName == null || lastName.isEmpty()) {
            System.out.println("The last name cannot be null or empty.");
            return false;
        }

        if (!lastName.matches(regex)) {
            System.out.println("The last name can only contain letters and spaces.");
            return false;
        }
        return true;
    }

    /**
     * Validates if the given option is within the accepted range (1-5).
     * @param number The option number to validate.
     */
    public static void isValidOption(String number) {
        String regex = "^[1-5]$";
        if (number == null || number.isEmpty()) {
            System.out.println("The option cannot be null or empty.");
        }
        if (!number.matches(regex)) {
            System.out.println("The option must be between 1 and 5.");
        }
    }
}
