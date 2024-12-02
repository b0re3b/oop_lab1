package com.creditapp.Service;

import com.creditapp.DTO.ActiveCreditDTO;
import com.creditapp.DTO.UserDTO;
import com.creditapp.Model.*;
import com.creditapp.Repository.CreditRepository;
import com.creditapp.Repository.CreditRepositoryImpl;
import com.creditapp.Repository.UserRepository;
import com.creditapp.util.CreditCalculator;
import com.creditapp.util.ValidationUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class that handles operations related to credit offers, credit applications, and active credits.
 * It provides functionality for creating, saving, deleting, and processing credit offers and active credits.
 */
@Service
public class CreditService {

    private final CreditRepository creditRepository; // Repository for handling CreditOffer
    private final CreditRepositoryImpl creditRepositoryImpl; // Custom repository for handling advanced credit logic
    private final UserService userService; // Service for handling user-related operations
    private final CreditCalculator creditCalculator; // Utility for calculating monthly payments
    private final ValidationUtil validationUtil; // Utility for validating credit parameters
    private final UserRepository userRepository; // Repository for handling user-related operations
    private final CreditRepositoryImpl impl; // Another instance of CreditRepositoryImpl for querying offers by status

    @Autowired
    public CreditService(
            CreditRepository creditRepository,
            CreditRepositoryImpl creditRepositoryImpl,
            UserService userService,
            CreditCalculator creditCalculator,
            ValidationUtil validationUtil,
            UserRepository userRepository,
            CreditRepositoryImpl impl
    ) {
        this.creditRepository = creditRepository;
        this.creditRepositoryImpl = creditRepositoryImpl;
        this.userService = userService;
        this.creditCalculator = creditCalculator;
        this.validationUtil = validationUtil;
        this.userRepository = userRepository;
        this.impl = impl;
    }

    /**
     * Saves a new credit offer or updates an existing one.
     * @param creditOffer The credit offer to save or update.
     * @return The saved or updated CreditOffer.
     */
    @Transactional
    public CreditOffer saveCreditOffer(CreditOffer creditOffer) {
        return creditRepository.save(creditOffer);
    }

    /**
     * Deletes a credit offer by its ID.
     * @param id The ID of the credit offer to delete.
     * @return true if the credit offer was deleted successfully, false if not found.
     */
    @Transactional
    public boolean deleteCreditOffer(Long id) {
        return creditRepository.findById(id)
                .map(offer -> {
                    creditRepository.delete(offer);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Creates a new credit offer based on bank, credit type, amount, and term.
     * @param bankId The ID of the bank offering the credit.
     * @param typeId The ID of the credit type.
     * @param amount The amount of credit requested.
     * @param termMonths The term (in months) for which the credit is requested.
     * @return The newly created CreditOffer.
     */
    @Transactional
    public CreditOffer createCreditOffer(Long bankId, long typeId, BigDecimal amount, int termMonths) {
        Bank bank = creditRepository.findBankById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Bank not found"));

        Credit creditType = userRepository.findCreditTypeById(typeId)
                .orElseThrow(() -> new IllegalArgumentException("Credit type not found"));

        validateCreditParameters(amount, termMonths);

        BigDecimal creditRating = calculateCreditRating(bank);
        BigDecimal interestRate = calculateInterestRate(creditRating, amount, termMonths);

        CreditOffer creditOffer = new CreditOffer();
        creditOffer.setBank(bank);
        creditOffer.setCreditType(creditType);
        creditOffer.setInterestRate(interestRate);
        creditOffer.setEarlyRepaymentAllowed(true);
        creditOffer.setCreditLineIncreaseAllowed(false);

        ActiveCredit activeCredit = createActiveCredit(creditOffer, amount, termMonths,
                creditCalculator.calculateMonthlyPayment(amount, interestRate, termMonths));

        creditOffer.addActiveCredit(activeCredit);

        return creditRepository.save(creditOffer);
    }

    /**
     * Creates an ActiveCredit associated with a given CreditOffer.
     * @param creditOffer The credit offer for the active credit.
     * @param amount The loan amount.
     * @param termMonths The loan term in months.
     * @param monthlyPayment The calculated monthly payment.
     * @return The created ActiveCredit.
     */
    private ActiveCredit createActiveCredit(CreditOffer creditOffer, BigDecimal amount, int termMonths, BigDecimal monthlyPayment) {
        ActiveCredit activeCredit = new ActiveCredit();
        activeCredit.setCreditOffer(creditOffer);
        activeCredit.setLoanAmount(amount);
        activeCredit.setLoanTermMonths(termMonths);
        activeCredit.setMonthlyPayment(monthlyPayment);
        activeCredit.setStatus(ActiveCredit.Status.PENDING);

        LocalDate now = LocalDate.now();
        activeCredit.setStartDate(now);
        activeCredit.setEndDate(now.plusMonths(termMonths));
        activeCredit.setRemainingBalance(amount);

        return activeCredit;
    }

    /**
     * Finds suitable credit offers for a customer based on the desired loan amount and term.
     * @param customerId The ID of the customer.
     * @param desiredAmount The desired loan amount.
     * @param desiredTermMonths The desired loan term in months.
     * @return A list of credit offers that match the desired parameters.
     */
    public List<CreditOffer> findSuitableCreditOffers(long customerId, BigDecimal desiredAmount, int desiredTermMonths) {
        validateInputParameters(customerId, desiredAmount, desiredTermMonths);

        // Retrieve all credit offers for the customer
        List<CreditOffer> allOffers = creditRepository.findCreditOffersByCustomerId(customerId);

        // Filter offers based on conditions
        return allOffers.stream()
                .filter(offer -> offer.getActiveCredits() != null &&
                        !offer.getActiveCredits().isEmpty() &&
                        offer.getActiveCredits().stream().anyMatch(ac ->
                                ac.getLoanAmount().compareTo(desiredAmount) >= 0 &&
                                        ac.getLoanTermMonths() >= desiredTermMonths))
                .collect(Collectors.toList());
    }

    /**
     * Validates the input parameters for credit application.
     * @param customerId The customer ID.
     * @param desiredAmount The desired loan amount.
     * @param desiredTermMonths The desired loan term.
     */
    private void validateInputParameters(long customerId, BigDecimal desiredAmount, int desiredTermMonths) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }
        if (desiredAmount == null || desiredAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Desired amount must be greater than zero.");
        }
        if (desiredTermMonths <= 0) {
            throw new IllegalArgumentException("Desired term must be greater than zero.");
        }
    }

    /**
     * Validates credit parameters, ensuring they are within acceptable limits.
     * @param amount The credit amount.
     * @param termMonths The credit term in months.
     */
    private void validateCreditParameters(BigDecimal amount, int termMonths) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        if (termMonths < 12 || termMonths > 360) {
            throw new IllegalArgumentException("Credit term must be between 12 and 360 months");
        }
        validationUtil.validateCreditAmount(amount.doubleValue(), 0, 100000);
        validationUtil.validateCreditTerm(termMonths, 12, 360);
    }

    /**
     * Calculates the credit rating of a bank.
     * @param bank The bank whose credit rating is being calculated.
     * @return The calculated credit rating.
     */
    private BigDecimal calculateCreditRating(Bank bank) {
        return new BigDecimal("500.0");
    }

    /**
     * Calculates the credit rating of a user.
     * @param user The user whose credit rating is being calculated.
     * @return The calculated credit rating.
     */
    private BigDecimal calculateCreditRating(User user) {
        return user.getCreditRating() != null
                ? new BigDecimal(user.getCreditRating())
                : new BigDecimal("500.0");
    }

    /**
     * Calculates the interest rate based on the credit rating, loan amount, and loan term.
     * @param creditRating The credit rating.
     * @param amount The loan amount.
     * @param termMonths The loan term in months.
     * @return The calculated interest rate.
     */
    private BigDecimal calculateInterestRate(BigDecimal creditRating, BigDecimal amount, int termMonths) {
        BigDecimal interestRate = new BigDecimal("5.0");

        if (creditRating.compareTo(new BigDecimal("700")) > 0) {
            interestRate = interestRate.subtract(new BigDecimal("1.0"));
        } else if (creditRating.compareTo(new BigDecimal("600")) < 0) {
            interestRate = interestRate.add(new BigDecimal("2.0"));
        }

        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            interestRate = interestRate.add(new BigDecimal("1.0"));
        }
        if (termMonths > 60) {
            interestRate = interestRate.add(new BigDecimal("1.0"));
        }

        return interestRate;
    }

    /**
     * Processes a credit application, either approving or rejecting it.
     * @param offerId The ID of the credit offer.
     * @param isApproved Whether the application is approved.
     * @return The updated active credit with the new status.
     */
    @Transactional
    public ActiveCredit processCreditApplication(long offerId, boolean isApproved) {
        CreditOffer creditOffer = creditRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Credit offer not found"));

        ActiveCredit activeCredit = creditOffer.getActiveCredits().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active credit found for offer"));

        activeCredit.setStatus(isApproved
                ? ActiveCredit.Status.APPROVED
                : ActiveCredit.Status.REJECTED);

        return creditRepositoryImpl.saveActiveCredit(activeCredit);
    }

    /**
     * Calculates the early repayment amount for an approved credit.
     * @param offerId The ID of the credit offer.
     * @return The early repayment amount.
     */
    public BigDecimal calculateEarlyRepayment(long offerId) {
        CreditOffer creditOffer = creditRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Credit offer not found"));

        ActiveCredit activeCredit = creditOffer.getActiveCredits().stream()
                .filter(ac -> ac.getStatus() == ActiveCredit.Status.APPROVED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No approved active credit for this offer"));

        BigDecimal remainingPrincipal = activeCredit.getRemainingBalance();
        BigDecimal interestRate = creditOffer.getInterestRate();
        int remainingMonths = activeCredit.getLoanTermMonths();

        BigDecimal remainingInterest = remainingPrincipal
                .multiply(interestRate.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(remainingMonths))
                .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);

        return remainingPrincipal.add(remainingInterest);
    }

    /**
     * Retrieves the credit offers with active credits that are approved for a given customer.
     * @param customerId The ID of the customer.
     * @return A list of credit offers with approved active credits.
     */
    public List<CreditOffer> getActiveCreditsForCustomer(long customerId) {
        return creditRepository.findCreditOffersByCustomerId(customerId).stream()
                .filter(offer -> offer.getActiveCredits() != null &&
                        offer.getActiveCredits().stream()
                                .anyMatch(activeCredit ->
                                        activeCredit.getStatus() != null &&
                                                ActiveCreditDTO.Status.APPROVED.name().equals(activeCredit.getStatus())))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the credit offers with active credits that are either pending or approved for a given customer.
     * @param customerId The ID of the customer.
     * @return A list of credit offers with active credits that are pending or approved.
     */
    public List<CreditOffer> getActiveCreditOffers(long customerId) {
        return creditRepository.findCreditOffersByCustomerId(customerId).stream()
                .filter(offer -> offer.getActiveCredits() != null &&
                        offer.getActiveCredits().stream()
                                .anyMatch(activeCredit ->
                                        activeCredit.getStatus() != null &&
                                                (ActiveCreditDTO.Status.PENDING.name().equals(activeCredit.getStatus()) ||
                                                        ActiveCreditDTO.Status.APPROVED.name().equals(activeCredit.getStatus()))))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves credit offers by their status using the custom repository implementation.
     * @param status The status of the credit offers to retrieve.
     * @return A list of credit offers with the specified status.
     */
    public List<CreditOffer> getCreditOffersByStatus(ActiveCreditDTO.Status status) {
        return impl.getCreditOffersByStatus(status.name());
    }

    /**
     * Maps a UserDTO to a User entity.
     * @param userDTO The UserDTO object to map.
     * @return The mapped User entity.
     */
    private User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setCustomerId(userDTO.getCustomerId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setMonthlyIncome(userDTO.getMonthlyIncome());
        user.setCreditRating(userDTO.getCreditRating());
        return user;
    }
}
