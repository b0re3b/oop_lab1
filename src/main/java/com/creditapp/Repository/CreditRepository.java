package com.creditapp.Repository;

import com.creditapp.Model.CreditOffer;
import com.creditapp.Model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for interacting with credit offers in the database.
 * Uses Spring Data JPA for data persistence operations.
 */
@Repository
public interface CreditRepository extends JpaRepository<CreditOffer, Long> {

    /**
     * Finds credit offers associated with a specific customer by their ID.
     * @param customerId The ID of the customer.
     * @return A list of credit offers associated with the customer.
     */
    @Query("SELECT ac.creditOffer FROM active_credit ac WHERE ac.customer.userId = :customerId")
    List<CreditOffer> findCreditOffersByCustomerId(@Param("customerId") Long customerId);

    /**
     * Finds a credit offer by its ID.
     * @param id The ID of the credit offer.
     * @return An Optional containing the credit offer, if found.
     */
    Optional<CreditOffer> findById(Long id);

    /**
     * Finds all credit offers in the database.
     * @return A list of all credit offers.
     */
    List<CreditOffer> findAll();

    /**
     * Checks if a credit offer exists by its ID.
     * @param id The ID of the credit offer.
     * @return true if the credit offer exists, false otherwise.
     */
    boolean existsById(Long id);

    /**
     * Finds credit offers by their status.
     * @param status The status of the credit offer.
     * @return A list of credit offers with the specified status.
     */
    List<CreditOffer> findByStatus(String status);

    /**
     * Finds a bank by its ID.
     * @param bankId The ID of the bank.
     * @return An Optional containing the bank, if found.
     */
    Optional<Bank> findBankById(Long bankId);
}
