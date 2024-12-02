package com.creditapp.Controller;

import com.creditapp.DTO.UserDTO;
import com.creditapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for managing user-related operations such as registration, profile updates,
 * and credit rating calculations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for injecting the UserService dependency.
     *
     * @param userService The UserService used by this controller.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param firstName     The user's first name.
     * @param lastName      The user's last name.
     * @param email         The user's email address.
     * @param phoneNumber   The user's phone number.
     * @param passportNumber The user's passport number.
     * @return A ResponseEntity containing the registered user or an error response.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String passportNumber) {
        try {
            if (!isValidRegistrationRequest(firstName, lastName, email, phoneNumber, passportNumber)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input", "Missing or incorrect fields"));
            }

            UserDTO registeredUser = userService.registerNewUser(firstName, lastName, email, phoneNumber, passportNumber);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Registration failed", e.getMessage()));
        }
    }

    /**
     * Retrieves the user profile for the given user ID.
     *
     * @param userId The ID of the user whose profile is being fetched.
     * @return A ResponseEntity containing the user profile or an error response.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable int userId) {
        try {
            UserDTO userProfile = userService.getUserProfile(userId);
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Updates the user profile with the given information.
     *
     * @param userId    The ID of the user to be updated.
     * @param email     The updated email (optional).
     * @param phoneNumber The updated phone number (optional).
     * @return A ResponseEntity containing the updated user profile or an error response.
     */
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable int userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber) {
        try {
            if (!isValidUpdateRequest(email, phoneNumber)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input", "Missing or incorrect fields"));
            }

            UserDTO updatedUser = userService.updateUserProfile(userId, email, phoneNumber);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Update failed", e.getMessage()));
        }
    }

    /**
     * Calculates the credit rating for a user based on their monthly income and existing credits.
     *
     * @param monthlyIncome The user's monthly income.
     * @param existingCredits The list of IDs of the user's existing credits.
     * @return A ResponseEntity containing the calculated credit rating or an error response.
     */
    @PostMapping("/credit-rating")
    public ResponseEntity<Integer> calculateCreditRating(
            @RequestParam BigDecimal monthlyIncome,
            @RequestParam List<Long> existingCredits) {
        try {
            int creditRating = userService.calculateCreditRating(monthlyIncome, existingCredits);
            return ResponseEntity.ok(creditRating);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Validates the registration request fields.
     *
     * @param firstName     The first name.
     * @param lastName      The last name.
     * @param email         The email address.
     * @param phoneNumber   The phone number.
     * @param passportNumber The passport number.
     * @return true if all required fields are valid, false otherwise.
     */
    private boolean isValidRegistrationRequest(String firstName, String lastName, String email, String phoneNumber, String passportNumber) {
        return firstName != null && !firstName.isEmpty() &&
                lastName != null && !lastName.isEmpty() &&
                email != null && !email.isEmpty() &&
                phoneNumber != null && !phoneNumber.isEmpty() &&
                passportNumber != null && !passportNumber.isEmpty();
    }

    /**
     * Validates the update request fields.
     *
     * @param email       The email address (optional).
     * @param phoneNumber The phone number (optional).
     * @return true if at least one of the fields is provided, false otherwise.
     */
    private boolean isValidUpdateRequest(String email, String phoneNumber) {
        return (email != null && !email.isEmpty()) || (phoneNumber != null && !phoneNumber.isEmpty());
    }

    /**
     * Represents an error response in case of failure.
     */
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
