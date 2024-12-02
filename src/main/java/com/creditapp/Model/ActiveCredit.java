package com.creditapp.Model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The ActiveCredit entity represents a user's active credit agreement.
 * This class includes details about the loan, such as the loan amount, term, payment schedule, and status.
 */
@Entity
@Table(name = "active_credits")
public class ActiveCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "active_credit_id")
    private Long activeCreditId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private CreditOffer credit_offer;

    @Column(name = "loan_amount")
    private static BigDecimal loan_amount;

    @Column(name = "loan_term_months")
    private Integer loan_term_months;

    @Column(name = "start_date")
    private LocalDate start_date;

    @Column(name = "end_date")
    private LocalDate end_date;

    @Column(name = "monthly_payment")
    private BigDecimal monthly_payment;

    @Column(name = "remaining_balance")
    private BigDecimal remaining_balance;

    @Enumerated(EnumType.STRING)  // Using Enum for status
    @Column(name = "status")
    private Status status;

    /**
     * Enum for credit status. Defines the possible states of the credit.
     * - PENDING: Credit is pending approval
     * - APPROVED: Credit has been approved
     * - REJECTED: Credit application was rejected
     * - PROCESSED: Credit application has been processed
     */
    public enum Status {
        PENDING,       // Очікується
        APPROVED,      // Затверджено
        REJECTED,      // Відхилено
        PROCESSED      // Оброблено
    }

    // Constructor without parameters (required for JPA)
    public ActiveCredit() {}

    /**
     * Constructor with parameters for initializing the ActiveCredit object.
     *
     * @param activeCreditId The unique ID of the active credit.
     * @param customer The user associated with the credit.
     * @param creditOffer The credit offer tied to the loan.
     * @param loanAmount The loan amount.
     * @param loanTermMonths The loan term in months.
     * @param startDate The start date of the credit agreement.
     * @param endDate The end date of the credit agreement.
     * @param monthlyPayment The monthly payment for the credit.
     * @param remainingBalance The remaining balance on the loan.
     * @param status The current status of the credit.
     */
    public ActiveCredit(Long activeCreditId, User customer, CreditOffer creditOffer,
                        BigDecimal loanAmount, Integer loanTermMonths, LocalDate startDate,
                        LocalDate endDate, BigDecimal monthlyPayment, BigDecimal remainingBalance, Status status) {
        this.activeCreditId = activeCreditId;
        this.customer = customer;
        this.credit_offer = creditOffer;
        this.loan_amount = loanAmount;
        this.loan_term_months = loanTermMonths;
        this.start_date = startDate;
        this.end_date = endDate;
        this.monthly_payment = monthlyPayment;
        this.remaining_balance = remainingBalance;
        this.status = status;
    }

    // Getters and Setters
    public Long getActiveCreditId() {
        return activeCreditId;
    }

    public void setActiveCreditId(Long activeCreditId) {
        this.activeCreditId = activeCreditId;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public CreditOffer getCreditOffer() {
        return credit_offer;
    }

    public void setCreditOffer(CreditOffer creditOffer) {
        this.credit_offer = creditOffer;
    }

    public static BigDecimal getLoanAmount() {
        return loan_amount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loan_amount = loanAmount;
    }

    public Integer getLoanTermMonths() {
        return loan_term_months;
    }

    public void setLoanTermMonths(Integer loanTermMonths) {
        this.loan_term_months = loanTermMonths;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public void setStartDate(LocalDate startDate) {
        this.start_date = startDate;
    }

    public LocalDate getEndDate() {
        return end_date;
    }

    public void setEndDate(LocalDate endDate) {
        this.end_date = endDate;
    }

    public BigDecimal getMonthlyPayment() {
        return monthly_payment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthly_payment = monthlyPayment;
    }

    public BigDecimal getRemainingBalance() {
        return remaining_balance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remaining_balance = remainingBalance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Provides a string representation of the ActiveCredit object.
     * This includes the active credit's ID, loan amount, term, dates, payment details, and status.
     *
     * @return A string containing the details of the active credit.
     */
    @Override
    public String toString() {
        return "ActiveCredit{" +
                "activeCreditId=" + activeCreditId +
                ", loanAmount=" + loan_amount +
                ", loanTermMonths=" + loan_term_months +
                ", startDate=" + start_date +
                ", endDate=" + end_date +
                ", monthlyPayment=" + monthly_payment +
                ", remainingBalance=" + remaining_balance +
                ", status=" + status +
                '}';
    }
}
