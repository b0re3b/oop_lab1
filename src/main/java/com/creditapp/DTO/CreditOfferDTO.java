package com.creditapp.DTO;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data Transfer Object (DTO) for representing a credit offer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditOfferDTO {

    @NotNull(message = "ID пропозиції не може бути порожнім")
    private Long offerId;

    @NotBlank(message = "Назва банку є обов'язковою")
    private String bankName;

    @NotBlank(message = "Тип кредиту є обов'язковим")
    private String creditType;

    @NotNull(message = "Процентна ставка не може бути порожньою")
    @DecimalMin(value = "0.0", message = "Процентна ставка не може бути від'ємною")
    private BigDecimal interestRate;

    @Min(value = 1, message = "Мінімальний термін має бути більше 0")
    private int minTermMonths;

    @Min(value = 1, message = "Максимальний термін має бути більше 0")
    private int maxTermMonths;

    private boolean earlyRepaymentAllowed;

    // Default constructor
    public CreditOfferDTO() {}

    /**
     * Constructor to initialize a CreditOfferDTO with the given values.
     *
     * @param offerId            The unique ID of the credit offer.
     * @param bankName           The name of the bank offering the credit.
     * @param creditType         The type of the credit (e.g., personal loan, mortgage).
     * @param interestRate       The interest rate for the credit.
     * @param minTermMonths      The minimum term for the credit in months.
     * @param maxTermMonths      The maximum term for the credit in months.
     * @param earlyRepaymentAllowed Flag indicating if early repayment is allowed.
     */
    public CreditOfferDTO(Long offerId, String bankName, String creditType,
                          BigDecimal interestRate, int minTermMonths,
                          int maxTermMonths, boolean earlyRepaymentAllowed) {
        this.offerId = offerId;
        this.bankName = bankName;
        this.creditType = creditType;
        this.interestRate = interestRate;
        this.minTermMonths = minTermMonths;
        this.maxTermMonths = maxTermMonths;
        this.earlyRepaymentAllowed = earlyRepaymentAllowed;
    }

    // Getters and Setters
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getMinTermMonths() {
        return minTermMonths;
    }

    public void setMinTermMonths(int minTermMonths) {
        this.minTermMonths = minTermMonths;
    }

    public int getMaxTermMonths() {
        return maxTermMonths;
    }

    public void setMaxTermMonths(int maxTermMonths) {
        this.maxTermMonths = maxTermMonths;
    }

    public boolean isEarlyRepaymentAllowed() {
        return earlyRepaymentAllowed;
    }

    public void setEarlyRepaymentAllowed(boolean earlyRepaymentAllowed) {
        this.earlyRepaymentAllowed = earlyRepaymentAllowed;
    }

    /**
     * Validates if the requested term is within the allowed range.
     *
     * @param requestedTermMonths The requested term in months.
     * @return true if the requested term is within the minimum and maximum range, false otherwise.
     */
    public boolean isValidForTerm(int requestedTermMonths) {
        return requestedTermMonths >= minTermMonths && requestedTermMonths <= maxTermMonths;
    }

    /**
     * Calculates the monthly payment based on the given loan amount and term.
     * Uses a simplified formula for calculating the payment on a loan with fixed monthly payments.
     *
     * @param amount      The loan amount.
     * @param termMonths The term of the loan in months.
     * @return The calculated monthly payment.
     * @throws IllegalArgumentException if the requested term is outside the valid range.
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, int termMonths) {
        if (!isValidForTerm(termMonths)) {
            throw new IllegalArgumentException("Некоректний термін кредиту");
        }

        double monthlyRate = interestRate.doubleValue() / 12 / 100;
        double monthlyPayment = amount.doubleValue() *
                (monthlyRate * Math.pow(1 + monthlyRate, termMonths)) /
                (Math.pow(1 + monthlyRate, termMonths) - 1);

        return BigDecimal.valueOf(monthlyPayment).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Provides a string representation of the CreditOfferDTO object.
     *
     * @return A string containing the details of the credit offer.
     */
    @Override
    public String toString() {
        return "CreditOfferDTO{" +
                "offerId=" + offerId +
                ", bankName='" + bankName + '\'' +
                ", creditType='" + creditType + '\'' +
                ", interestRate=" + interestRate +
                ", minTermMonths=" + minTermMonths +
                ", maxTermMonths=" + maxTermMonths +
                ", earlyRepaymentAllowed=" + earlyRepaymentAllowed +
                '}';
    }
}
