package com.creditapp.Repository;

import com.creditapp.Model.CreditOffer;
import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.Bank;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepositoryCustom {

    /**
     * Retrieves a CreditOffer by its unique ID.
     *
     * @param id the ID of the credit offer.
     * @return an Optional containing the CreditOffer if found, or empty if not found.
     */
    Optional<CreditOffer> getCreditOfferById(Long id);

    /**
     * Retrieves all credit offers available in the system.
     *
     * @return a List of all CreditOffer objects.
     */
    List<CreditOffer> getAllCreditOffers();

    /**
     * Deletes a credit offer based on its ID.
     *
     * @param id the ID of the credit offer to be deleted.
     * @return true if the deletion was successful, false otherwise.
     */
    boolean deleteCreditOffer(Long id);

    /**
     * Checks if a credit offer exists in the system by its ID.
     *
     * @param id the ID of the credit offer.
     * @return true if the credit offer exists, false otherwise.
     */
    boolean creditOfferExists(Long id);

    /**
     * Retrieves a list of credit offers based on their status.
     *
     * @param status the status of the credit offers (e.g., "PENDING", "APPROVED").
     * @return a List of CreditOffer objects with the specified status.
     */
    List<CreditOffer> getCreditOffersByStatus(String status);

    /**
     * Finds a bank by its unique ID.
     *
     * @param bankId the ID of the bank.
     * @return an Optional containing the Bank if found, or empty if not found.
     */
    Optional<Bank> findBankById(Long bankId);

    /**
     * Saves an ActiveCredit object to the database.
     *
     * @param activeCredit the active credit object to be saved.
     * @return the saved ActiveCredit object.
     */
    ActiveCredit saveActiveCredit(ActiveCredit activeCredit);

    /**
     * Saves a CreditOffer object to the database.
     *
     * @param creditOffer the credit offer object to be saved.
     * @return the saved CreditOffer object.
     */
    CreditOffer saveCreditOffer(CreditOffer creditOffer);

    /**
     * Saves a Bank object to the database.
     *
     * @param bank the bank object to be saved.
     * @return the saved Bank object.
     */
    Bank saveBank(Bank bank);
}
