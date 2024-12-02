package com.creditapp.DTO;

import java.math.BigDecimal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for representing user information.
 * This object is used to transfer user data between different layers of the application.
 */
public class UserDTO {

    private Integer customerId;

    @NotBlank(message = "Ім'я не може бути порожнім")  // Translated validation message
    private String firstName;

    @NotBlank(message = "Прізвище не може бути порожнім")  // Translated validation message
    private String lastName;

    @Email(message = "Невірний формат електронної пошти")  // Translated validation message
    @NotBlank(message = "Електронна пошта не може бути порожньою")  // Translated validation message
    private String email;

    @NotBlank(message = "Номер телефону не може бути порожнім")  // Translated validation message
    private String phoneNumber;

    @NotNull(message = "Місячний дохід не може бути порожнім")  // Translated validation message
    private BigDecimal monthlyIncome;

    private Integer creditRating;

    /**
     * Constructor to initialize a UserDTO object with required values.
     *
     * @param customerId The unique customer ID.
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @param email      The email address of the user.
     * @param phoneNumber The phone number of the user.
     */
    public UserDTO(Integer customerId, String firstName, String lastName, String email, String phoneNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Integer getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(Integer creditRating) {
        this.creditRating = creditRating;
    }

    /**
     * Provides a string representation of the UserDTO object.
     * This includes the user's customer ID, first name, last name, email, phone number,
     * and optionally the monthly income and credit rating (if not null).
     *
     * @return A string containing the details of the user.
     */
    @Override
    public String toString() {
        return "UserDTO{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                (monthlyIncome != null ? ", monthlyIncome=" + monthlyIncome : "") +
                (creditRating != null ? ", creditRating=" + creditRating : "") +
                '}';
    }
}
