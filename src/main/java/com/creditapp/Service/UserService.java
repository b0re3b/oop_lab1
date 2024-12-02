package com.creditapp.Service;

import com.creditapp.DTO.UserDTO;
import com.creditapp.Model.User;
import com.creditapp.Repository.UserRepository;
import com.creditapp.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserService(UserRepository userRepository,

                       ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
    }

    /**
     * Registers a new user by validating the input data and saving the user to the database.
     *
     * @param firstName      the first name of the user.
     * @param lastName       the last name of the user.
     * @param email          the email of the user.
     * @param phoneNumber    the phone number of the user.
     * @param passportNumber the passport number of the user.
     * @return a UserDTO object containing the registered user's details.
     */
    @Transactional
    public UserDTO registerNewUser(String firstName, String lastName, String email,
                                   String phoneNumber, String passportNumber) {
        validateRegistrationData(email, passportNumber);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser.getCustomerId(), savedUser.getFirstName(),
                savedUser.getLastName(), savedUser.getEmail(), savedUser.getPhoneNumber());
    }

    /**
     * Retrieves the profile of a user by their customer ID.
     *
     * @param customerId the ID of the user.
     * @return a UserDTO object containing the user's profile details.
     */
    public UserDTO getUserProfile(int customerId) {
        User user = userRepository.findById((long) customerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserDTO(user.getCustomerId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.getPhoneNumber());
    }

    /**
     * Updates the profile information of a user by their user ID.
     *
     * @param userId      the ID of the user.
     * @param email       the new email of the user (optional).
     * @param phoneNumber the new phone number of the user (optional).
     * @return a UserDTO object containing the updated user's profile details.
     */
    @Transactional
    public UserDTO updateUserProfile(int userId, String email, String phoneNumber) {
        User user = userRepository.findById((long) userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update user details
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }

        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        User updatedUser = userRepository.save(user);

        return new UserDTO(updatedUser.getCustomerId(), updatedUser.getFirstName(),
                updatedUser.getLastName(), updatedUser.getEmail(), updatedUser.getPhoneNumber());
    }

    /**
     * Calculates the credit rating for a user based on their monthly income and existing credits.
     *
     * @param monthlyIncome   the monthly income of the user.
     * @param existingCredits a list of IDs representing the user's existing credits.
     * @return the calculated credit rating as an integer.
     */
    public Integer calculateCreditRating(BigDecimal monthlyIncome, List<Long> existingCredits) {
        if (monthlyIncome == null || existingCredits == null) {
            throw new IllegalArgumentException("Invalid data for credit rating calculation");
        }

        int baseRating = 700;

        if (monthlyIncome.compareTo(BigDecimal.valueOf(10000)) < 0) {
            baseRating -= 50;
        }

        if (!existingCredits.isEmpty()) {
            baseRating -= 20 * existingCredits.size();
        }

        return Math.max(300, Math.min(baseRating, 850));
    }

    /**
     * Validates the input data for user registration.
     *
     * @param email          the email of the user.
     * @param passportNumber the passport number of the user.
     */
    private void validateRegistrationData(String email, String passportNumber) {
        if (email == null || email.isEmpty() || passportNumber == null || passportNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid data for registration");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists");
        }

        if (userRepository.findByPassportNumber(passportNumber).isPresent()) {
            throw new IllegalArgumentException("A user with this passport number already exists");
        }
    }
}
