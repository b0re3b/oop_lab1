package com.creditapp.Repository;

import com.creditapp.Model.Credit;
import com.creditapp.Model.User;
import com.creditapp.Model.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a Credit type by its ID.
     *
     * @param id the ID of the credit type.
     * @return an Optional containing the Credit if found, or empty if not found.
     */
    Optional<Credit> findCreditTypeById(Long id);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user.
     * @return an Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds users by their last name.
     *
     * @param lastName the last name of the users to search for.
     * @return a List of Users with the specified last name.
     */
    List<User> findByLastName(String lastName);

    /**
     * Finds users by their phone number.
     *
     * @param phoneNumber the phone number to search for.
     * @return a List of Users with the specified phone number.
     */
    List<User> findByPhoneNumber(String phoneNumber);

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check.
     * @return true if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Finds users who have active credit applications.
     *
     * @return a List of Users with active credit applications.
     */
    @Query("SELECT DISTINCT u FROM customer u JOIN u.activeCredits ac")
    List<User> findUsersWithCreditApplications();

    /**
     * Finds users by their credit rating within a specified range.
     *
     * @param minScore the minimum credit rating.
     * @param maxScore the maximum credit rating.
     * @return a List of Users with credit ratings between the specified range.
     */
    List<User> findByCreditRatingBetween(int minScore, int maxScore);

    /**
     * Finds users with pending credit applications.
     *
     * @return a List of Users with pending credit applications.
     */
    @Query("SELECT DISTINCT u FROM customer u JOIN u.activeCredits ac WHERE ac.status = 'PENDING'")
    List<User> findUsersWithPendingCreditApplications();

    /**
     * Finds a user by their passport number.
     *
     * @param passportNumber the passport number of the user.
     * @return an Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByPassportNumber(String passportNumber);

    /**
     * Finds a CreditOffer by its unique ID.
     *
     * @param offerId the ID of the credit offer.
     * @return an Optional containing the CreditOffer if found, or empty if not found.
     */
    @Query("SELECT co FROM credit_offer co WHERE co.offerId = :offerId")
    Optional<CreditOffer> findCreditOfferById(@Param("offerId") Long offerId);

}
