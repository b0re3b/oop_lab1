package com.creditapp.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

/**
 * Represents a customer (user) in the credit application system.
 * This entity is mapped to the "customers" table in the database.
 */
@Entity
@Table(name = "customers") // Mapping this entity to the "customers" table in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    @Column(name = "customer_id") // Corresponding column in the database
    private Integer customer_id;

    @Column(name = "first_name") // First name of the user
    private String first_name;

    @Column(name = "last_name") // Last name of the user
    private String last_name;

    @Column(name = "birth_date") // Birth date of the user
    private Date birth_date;

    @Column(name = "passport_number", unique = true) // Passport number with unique constraint
    private String passport_number;

    @Column(name = "phone_number") // Phone number of the user
    private String phone_number;

    @Column(name = "email") // Email address of the user
    private String email;

    @Column(name = "monthly_income") // Monthly income of the user
    private BigDecimal monthly_income;

    @Column(name = "credit_rating") // Credit rating of the user
    private Integer credit_rating;

    // Default constructor
    public User() {}

    // Getters and Setters for the fields
    public Integer getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(Integer customerId) {
        this.customer_id = customerId;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public Date getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(Date birthDate) {
        this.birth_date = birthDate;
    }

    public String getPassportNumber() {
        return passport_number;
    }

    public void setPassportNumber(String passportNumber) {
        this.passport_number = passportNumber;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getMonthlyIncome() {
        return monthly_income;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthly_income = monthlyIncome;
    }

    public Integer getCreditRating() {
        return credit_rating;
    }

    public void setCreditRating(Integer creditRating) {
        this.credit_rating = creditRating;
    }

    /**
     * Overridden equals and hashCode methods to ensure correct comparison of User objects.
     * The objects are considered equal if both customerId and passportNumber match.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(customer_id, user.customer_id) &&
                Objects.equals(passport_number, user.passport_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer_id, passport_number);
    }

    /**
     * Overridden toString method for a readable representation of the User object.
     * This is helpful for debugging and logging.
     *
     * @return String representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" +
                "customerId=" + customer_id +
                ", firstName='" + first_name + '\'' +
                ", lastName='" + last_name + '\'' +
                ", birthDate=" + birth_date +
                ", passportNumber='" + passport_number + '\'' +
                ", phoneNumber='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", monthlyIncome=" + monthly_income +
                ", creditRating=" + credit_rating +
                '}';
    }
}
