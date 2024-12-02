package com.creditapp.Repository;

import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.Bank;
import com.creditapp.Model.CreditOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of custom repository methods for managing credit-related entities.
 * Provides custom logic for saving and deleting entities like ActiveCredit, Bank, and CreditOffer.
 */
@Repository
public class CreditRepositoryImpl implements CreditRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager; // EntityManager for direct database interaction

    @Autowired
    @Lazy
    private CreditRepository creditRepository; // Injects the CreditRepository for standard repository methods

    /**
     * Saves or updates an active credit entity.
     * If the active credit already exists, updates its details.
     * If it's new, persists it to the database.
     * @param activeCredit The active credit entity to save or update.
     * @return The saved or updated ActiveCredit entity.
     */
    @Transactional
    @Override
    public ActiveCredit saveActiveCredit(ActiveCredit activeCredit) {
        if (activeCredit.getActiveCreditId() != null) {
            ActiveCredit existingCredit = entityManager.find(ActiveCredit.class, activeCredit.getActiveCreditId());
            if (existingCredit != null) {
                existingCredit.setStatus(activeCredit.getStatus());
                existingCredit.setLoanAmount(activeCredit.getLoanAmount());
                existingCredit.setCreditOffer(activeCredit.getCreditOffer());
                return existingCredit;
            }
        }
        entityManager.persist(activeCredit); // Persist the new ActiveCredit
        return activeCredit;
    }

    /**
     * Saves or updates a bank entity.
     * If the bank already exists, updates its details.
     * If it's new, persists it to the database.
     * @param bank The bank entity to save or update.
     * @return The saved or updated Bank entity.
     */
    @Transactional
    @Override
    public Bank saveBank(Bank bank) {
        if (bank.getBankId() != null) {
            Bank existingBank = entityManager.find(Bank.class, bank.getBankId());
            if (existingBank != null) {
                existingBank.setBankName(bank.getBankName());
                existingBank.setBankCode(bank.getBankCode());
                existingBank.setContactPhone(bank.getContactPhone());
                existingBank.setWebsite(bank.getWebsite());
                return existingBank;
            }
        }
        entityManager.persist(bank); // Persist the new Bank
        return bank;
    }

    /**
     * Saves or updates a credit offer entity.
     * If the credit offer already exists, updates its details.
     * If it's new, persists it to the database.
     * @param creditOffer The credit offer entity to save or update.
     * @return The saved or updated CreditOffer entity.
     */
    @Transactional
    @Override
    public CreditOffer saveCreditOffer(CreditOffer creditOffer) {
        if (creditOffer.getOfferId() != null) {
            CreditOffer existingOffer = entityManager.find(CreditOffer.class, creditOffer.getOfferId());
            if (existingOffer != null) {
                existingOffer.setInterestRate(creditOffer.getInterestRate());
                existingOffer.setEarlyRepaymentAllowed(creditOffer.getEarlyRepaymentAllowed());
                existingOffer.setCreditLineIncreaseAllowed(creditOffer.getCreditLineIncreaseAllowed());
                existingOffer.setDescription(creditOffer.getDescription());
                existingOffer.setBank(creditOffer.getBank());
                existingOffer.setCreditType(creditOffer.getCreditType());
                return existingOffer;
            }
        }
        entityManager.persist(creditOffer); // Persist the new CreditOffer
        return creditOffer;
    }

    /**
     * Retrieves a credit offer by its ID using the standard repository.
     * @param id The ID of the credit offer.
     * @return An Optional containing the credit offer, if found.
     */
    @Override
    public Optional<CreditOffer> getCreditOfferById(Long id) {
        return creditRepository.findById(id);
    }

    /**
     * Retrieves all credit offers from the database.
     * @return A list of all credit offers.
     */
    @Override
    public List<CreditOffer> getAllCreditOffers() {
        return creditRepository.findAll();
    }

    /**
     * Deletes a credit offer by its ID.
     * @param id The ID of the credit offer to delete.
     * @return true if the credit offer was successfully deleted, false otherwise.
     */
    @Override
    @Transactional
    public boolean deleteCreditOffer(Long id) {
        if (creditRepository.existsById(id)) {
            creditRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Checks whether a credit offer exists by its ID.
     * @param id The ID of the credit offer.
     * @return true if the credit offer exists, false otherwise.
     */
    @Override
    public boolean creditOfferExists(Long id) {
        return creditRepository.existsById(id);
    }

    /**
     * Retrieves credit offers based on their status.
     * @param status The status of the credit offers to retrieve.
     * @return A list of credit offers matching the given status.
     */
    @Override
    public List<CreditOffer> getCreditOffersByStatus(String status) {
        return creditRepository.findByStatus(status);
    }

    /**
     * Retrieves a bank by its ID using direct EntityManager query.
     * @param bankId The ID of the bank to retrieve.
     * @return An Optional containing the bank, if found.
     */
    @Override
    public Optional<Bank> findBankById(Long bankId) {
        return Optional.ofNullable(entityManager.find(Bank.class, bankId));
    }
}
