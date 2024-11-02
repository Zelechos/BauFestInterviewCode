package module;

import java.time.LocalDate;

/**
 * Represents a person with personal information including first name, last name,
 * identification number (DNI), birth date, and email address.
 */
public class Person {
    private String firstName;  // The first name of the person.
    private String lastName;   // The last name of the person.
    private String dni;        // The identification number of the person.
    private LocalDate birthDate; // The birth date of the person.
    private String email;      // The email address of the person.

    /**
     * Constructs a new Person object with the specified details.
     *
     * @param firstName The first name of the person.
     * @param lastName The last name of the person.
     * @param dni The identification number of the person.
     * @param birthDate The birth date of the person.
     * @param email The email address of the person.
     */
    public Person(String firstName, String lastName, String dni, LocalDate birthDate, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.birthDate = birthDate;
        this.email = email;
    }

    /**
     * Returns the first name of the person.
     *
     * @return The first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the person.
     *
     * @param firstName The new first name of the person.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the person.
     *
     * @return The last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the person.
     *
     * @param lastName The new last name of the person.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the identification number (DNI) of the person.
     *
     * @return The identification number of the person.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets the identification number (DNI) of the person.
     *
     * @param dni The new identification number of the person.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Returns the birth date of the person.
     *
     * @return The birth date of the person.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the person.
     *
     * @param birthDate The new birth date of the person.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the email address of the person.
     *
     * @return The email address of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the person.
     *
     * @param email The new email address of the person.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
