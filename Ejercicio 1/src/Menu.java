import helpers.Validations;
import module.CreditCard;
import module.CreditCardService;
import module.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menu class that provides a user interface for registering persons,
 * credit cards, retrieving cards by DNI, and checking rates by date.
 */
public class Menu {
    // Mapa que almacena personas registradas usando su DNI como clave
    private static Map<String, Person> people = new HashMap<>();

    // Servicio para manejar operaciones relacionadas con tarjetas de crédito
    private static CreditCardService cardService = new CreditCardService();

    // Scanner para leer entradas del usuario
    private static Scanner scanner = new Scanner(System.in);

    // Formato para parsear fechas en el formato dd-MM-yyyy
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Main method that initiates the application and displays the main menu.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Bucle infinito para mostrar el menú hasta que el usuario decida salir
        while (true) {
            // Mostrar opciones del menú
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register Person");
            System.out.println("2. Register Credit Card");
            System.out.println("3. Get Cards by DNI");
            System.out.println("4. Check Rates by Date");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine(); // Leer opción del usuario
            Validations.isValidOption(option); // Validar la opción ingresada

            // Manejo de opciones seleccionadas
            switch (option) {
                case "1":
                    registerPerson(); // Llamar método para registrar persona
                    break;
                case "2":
                    registerCreditCard(); // Llamar método para registrar tarjeta de crédito
                    break;
                case "3":
                    getCardsByDni(); // Llamar método para obtener tarjetas por DNI
                    break;
                case "4":
                    checkRatesByDate(); // Llamar método para verificar tasas por fecha
                    break;
                case "5":
                    System.out.println("Exiting..."); // Mensaje de salida
                    return; // Salir del bucle y finalizar el programa
                default:
                    System.err.println("Invalid option. Try again."); // Mensaje de error por opción inválida
            }
        }
    }

    /**
     * Registers a new person by prompting the user for their details.
     * Validates the input and adds the person to the registry.
     */
    private static void registerPerson() {
        try {
            // Solicitar y leer el nombre de pila
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            if (!Validations.isValidName(firstName))
                throw new IllegalArgumentException("Name is Invalid."); // Validar nombre

            // Solicitar y leer el apellido
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            if (!Validations.isValidLastName(lastName))
                throw new IllegalArgumentException("LastName is Invalid."); // Validar apellido

            // Solicitar y leer el DNI
            System.out.print("Enter DNI: ");
            String dni = scanner.nextLine();
            if (people.containsKey(dni))
                throw new IllegalArgumentException("DNI already registered."); // Comprobar si el DNI ya está registrado

            if(!Validations.isValidDni(dni))
                throw new IllegalArgumentException("DNI is not Valid please enter numbers in the range [0-9]"); // Validar DNI

            // Solicitar y leer la fecha de nacimiento
            System.out.print("Enter Birth Date (dd-MM-yyyy): ");
            String dateInput = scanner.nextLine();
            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(dateInput, formatter); // Parsear fecha de nacimiento
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid birth date format. Please use dd-MM-yyyy."); // Manejo de error de fecha inválida
            }

            // Solicitar y leer el correo electrónico
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            if (!Validations.isValidEmail(email))
                throw new IllegalArgumentException("Invalid email format."); // Validar formato de correo electrónico

            // Crear un nuevo objeto Person y agregarlo al mapa
            Person person = new Person(firstName, lastName, dni, birthDate, email);
            people.put(dni, person);
            System.out.println("Person registered successfully."); // Mensaje de éxito

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage()); // Mensaje de error específico
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage()); // Mensaje de error general
        }
    }

    /**
     * Registers a new credit card by prompting the user for card details.
     * Validates the input and associates the card with a registered person.
     */
    private static void registerCreditCard() {
        try {
            // Solicitar y leer la marca de la tarjeta
            System.out.print("Enter Card Brand (VISA, NARA, AMEX): ");
            String brand = scanner.nextLine().toUpperCase();
            if (!Validations.isValidBrand(brand)) {
                throw new IllegalArgumentException("Invalid card brand. Accepted brands: VISA, NARA, AMEX."); // Validar marca
            }

            // Solicitar y leer el número de la tarjeta
            System.out.print("Enter Card Number: ");
            String cardNumber = scanner.nextLine();
            if (!Validations.isValidCardNumber(cardNumber)) {
                throw new IllegalArgumentException("Invalid card number format."); // Validar número de tarjeta
            }

            // Solicitar y leer la fecha de expiración
            System.out.print("Enter Expiration Date (dd-MM-yyyy): ");
            String dateInput = scanner.nextLine();
            LocalDate expirationDate;
            try {
                expirationDate = LocalDate.parse(dateInput, formatter); // Parsear fecha de expiración
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid expiration date format. Please use dd-MM-yyyy."); // Manejo de error de fecha inválida
            }

            // Solicitar y leer el nombre del titular de la tarjeta
            System.out.print("Enter Card Holder Name: ");
            String cardHolderName = scanner.nextLine();

            // Solicitar y leer el CVV
            System.out.print("Enter CVV: ");
            String cvv = scanner.nextLine();
            if (!Validations.isValidCVV(cvv)) {
                throw new IllegalArgumentException("Invalid CVV format. CVV must be 3 digits."); // Validar formato de CVV
            }

            // Solicitar y leer el DNI del titular
            System.out.print("Enter Holder DNI: ");
            String dni = scanner.nextLine();
            Person owner = people.get(dni); // Obtener la persona a partir del DNI
            if (owner == null) {
                throw new IllegalArgumentException("Person not found. Register the person first."); // Verificar si la persona existe
            }

            // Crear un nuevo objeto CreditCard y registrarlo en el servicio
            CreditCard card = new CreditCard(brand, cardNumber, cardHolderName, expirationDate, cvv, owner);
            cardService.registerCard(card);
            System.out.println("Credit card registered successfully."); // Mensaje de éxito

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage()); // Mensaje de error específico
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage()); // Mensaje de error general
        }
    }

    /**
     * Retrieves and displays credit cards associated with a person by their DNI.
     */
    private static void getCardsByDni() {
        try {
            // Solicitar y leer el DNI
            System.out.print("Enter DNI: ");
            String dni = scanner.nextLine().trim();

            if (!Validations.isValidDni(dni)) {
                throw new IllegalArgumentException("Invalid DNI format. Please enter a valid DNI."); // Validar formato de DNI
            }

            Person person = people.get(dni); // Obtener la persona a partir del DNI
            if (person == null) {
                System.out.println("Person not found."); // Mensaje si la persona no fue encontrada
                return;
            }

            List<CreditCard> cards = cardService.getCardsByPerson(person); // Obtener las tarjetas de la persona
            if (cards.isEmpty()) {
                System.out.println("No cards registered for " + person.getFirstName() + " " + person.getLastName() + "."); // Mensaje si no hay tarjetas registradas
                return;
            }

            // Mostrar las tarjetas registradas para la persona
            System.out.println("Cards for " + person.getFirstName() + " " + person.getLastName() + ":");
            cards.forEach(card -> System.out.println("Card Number: " + card.getCardNumber() + ", Brand: " + card.getBrand() + ", Expiration: " + card.getExpirationDate()));

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage()); // Mensaje de error específico
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage()); // Mensaje de error general
        }
    }

    /**
     * Checks and displays credit card rates for a specific date.
     * Prompts the user to enter a date and retrieves rates for each card brand.
     */
    private static void checkRatesByDate() {
        try {
            // Solicitar y leer la fecha
            System.out.print("Enter date to check rates (dd-MM-yyyy): ");
            String dateInput = scanner.nextLine();
            LocalDate date;
            try {
                date = LocalDate.parse(dateInput, formatter); // Parsear fecha
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy."); // Manejo de error de fecha inválida
            }

            // Mostrar tasas para cada marca de tarjeta
            System.out.println("Rates for date: " + date);
            System.out.println("VISA Rate: " + cardService.calculateRate("VISA", date)); // Mostrar tasa VISA
            System.out.println("NARA Rate: " + cardService.calculateRate("NARA", date)); // Mostrar tasa NARA
            System.out.println("AMEX Rate: " + cardService.calculateRate("AMEX", date)); // Mostrar tasa AMEX

        } catch (DateTimeParseException e) {
            // Manejo de error si la fecha ingresada es inválida
            System.err.println("Error: Invalid date. Please use the format dd-MM-yyyy."); // Mensaje de error
            System.err.println("Error: Date has erroneous days, months, or years. Please use the format dd(1-31)-MM(1-12)-yyyy."); // Mensaje de error
            System.err.println("Details of the error: " + e.getMessage()); // Detalles del error
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage()); // Mensaje de error específico
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage()); // Mensaje de error general
        }
    }
}
