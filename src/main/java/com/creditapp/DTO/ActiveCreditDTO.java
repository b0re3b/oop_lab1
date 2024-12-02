package com.creditapp.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for representing active credit details.
 */
public class ActiveCreditDTO {
    private Long creditId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Remaining balance must be greater than 0")
    private BigDecimal remainingBalance;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly payment must be greater than 0")
    private BigDecimal monthlyPayment;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Status status;

    /**
     * Enum representing the various states a credit can have.
     */
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED,
        PROCESSED
    }

    /**
     * Constructor to initialize the ActiveCreditDTO with the given values.
     *
     * @param creditId        The ID of the credit.
     * @param loanAmount      The total loan amount.
     * @param remainingBalance The remaining balance on the loan.
     * @param monthlyPayment  The monthly payment amount.
     * @param startDate       The start date of the credit.
     * @param endDate         The end date of the credit.
     * @param status          The current status of the credit.
     */
    public ActiveCreditDTO(Long creditId, BigDecimal loanAmount, BigDecimal remainingBalance, BigDecimal monthlyPayment, LocalDate startDate, LocalDate endDate, Status status) {
        this.creditId = creditId;
        this.loanAmount = loanAmount;
        this.remainingBalance = remainingBalance;
        this.monthlyPayment = monthlyPayment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getCreditId() {
        return creditId;
    }

    public void setCreditId(Long creditId) {
        this.creditId = creditId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Provides a string representation of the ActiveCreditDTO object.
     * Useful for debugging and logging.
     */
    @Override
    public String toString() {
        return "ActiveCreditDTO{" +
                "creditId=" + creditId +
                ", loanAmount=" + loanAmount +
                ", remainingBalance=" + remainingBalance +
                ", monthlyPayment=" + monthlyPayment +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
