package com.creditapp.util;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CreditCalculator {

    private static final int ROUNDING_SCALE = 2;

    public BigDecimal calculateMonthlyPayment(
            BigDecimal principal,
            BigDecimal annualInterestRate,
            int termMonths
    ) {
        if (principal.compareTo(BigDecimal.ZERO) <= 0 ||
                annualInterestRate.compareTo(BigDecimal.ZERO) < 0 ||
                termMonths <= 0) {
            throw new IllegalArgumentException("Invalid credit parameters");
        }

        BigDecimal monthlyRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 12), ROUNDING_SCALE, RoundingMode.HALF_UP);
        int numberOfPayments = termMonths;

        BigDecimal monthlyPayment = principal
                .multiply(monthlyRate.add(BigDecimal.ONE).pow(numberOfPayments))
                .divide(
                        BigDecimal.ONE.add(monthlyRate).pow(numberOfPayments).subtract(BigDecimal.ONE),
                        ROUNDING_SCALE,
                        RoundingMode.HALF_UP
                );

        return monthlyPayment;
    }

    public BigDecimal calculateTotalInterest(
            BigDecimal principal,
            BigDecimal annualInterestRate,
            int termMonths
    ) {
        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, annualInterestRate, termMonths);
        BigDecimal totalPayments = monthlyPayment.multiply(BigDecimal.valueOf(termMonths));
        return totalPayments.subtract(principal).setScale(ROUNDING_SCALE, RoundingMode.HALF_UP);
    }

    public int calculateCreditEligibility(
            BigDecimal monthlyIncome,
            int creditScore,
            BigDecimal requestedLoanAmount
    ) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) <= 0 ||
                creditScore < 300 ||
                creditScore > 850 ||
                requestedLoanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid credit parameters");
        }

        BigDecimal maxRecommendedMonthlyDebt = monthlyIncome.multiply(BigDecimal.valueOf(0.36));
        BigDecimal monthlyLoanPayment = calculateMonthlyPayment(
                requestedLoanAmount,
                new BigDecimal("5.0"),
                60
        );

        double scoreComponent = (creditScore - 300.0) / 550.0 * 50;

        double dtiComponent = 0;
        if (monthlyLoanPayment.compareTo(maxRecommendedMonthlyDebt) <= 0) {
            dtiComponent = 50;
        } else {
            dtiComponent = Math.max(0, 50 - (monthlyLoanPayment.doubleValue() / maxRecommendedMonthlyDebt.doubleValue() * 50));
        }

        int eligibilityScore = (int) Math.round(scoreComponent + dtiComponent);
        return Math.max(0, Math.min(100, eligibilityScore));
    }

    public BigDecimal calculateMaxLoanAmount(BigDecimal monthlyIncome) {
        return monthlyIncome
                .multiply(BigDecimal.valueOf(12 * 4))
                .setScale(ROUNDING_SCALE, RoundingMode.HALF_DOWN);
    }
}