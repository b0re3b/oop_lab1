package testservice;

import com.creditapp.DTO.UserDTO;
import com.creditapp.Model.User;
import com.creditapp.Repository.UserRepository;
import com.creditapp.Service.UserService;
import com.creditapp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerNewUser_Successfully() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phoneNumber = "+1234567890";
        String passportNumber = "AB123456";

        User savedUser = new User();
        savedUser.setCustomerId(Math.toIntExact(1L));
        savedUser.setFirstName(firstName);
        savedUser.setLastName(lastName);
        savedUser.setEmail(email);
        savedUser.setPhoneNumber(phoneNumber);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByPassportNumber(passportNumber)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(passportNumber)).thenReturn("encodedPassport");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = userService.registerNewUser(firstName, lastName, email, phoneNumber, passportNumber);

        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals(phoneNumber, result.getPhoneNumber());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerNewUser_ExistingEmail_ThrowsException() {
        // Arrange
        String email = "existing@example.com";
        String passportNumber = "AB123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                userService.registerNewUser("John", "Doe", email, "+1234567890", passportNumber)
        );
    }

    @Test
    void getUserProfile_ExistingUser_ReturnsUserDTO() {
        // Arrange
        int customerId = 1;
        User user = new User();
        user.setCustomerId(customerId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("+1234567890");

        when(userRepository.findById((long) customerId)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUserProfile(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void calculateCreditRating_NormalScenario() {
        // Arrange
        BigDecimal monthlyIncome = BigDecimal.valueOf(15000);
        List<Long> existingCredits = Arrays.asList(1L, 2L);

        // Act
        Integer creditRating = userService.calculateCreditRating(monthlyIncome, existingCredits);

        // Assert
        assertEquals(660, creditRating);
    }

    @Test
    void calculateCreditRating_LowIncome_ReducedRating() {
        // Arrange
        BigDecimal monthlyIncome = BigDecimal.valueOf(5000);
        List<Long> existingCredits = Arrays.asList(1L);

        // Act
        Integer creditRating = userService.calculateCreditRating(monthlyIncome, existingCredits);

        // Assert
        assertEquals(630, creditRating);
    }

    @Test
    void updateUserProfile_ValidUpdate_ReturnsUpdatedUserDTO() {
        // Arrange
        int userId = 1;
        String newEmail = "new.email@example.com";
        String newPhoneNumber = "+9876543210";

        User existingUser = new User();
        existingUser.setCustomerId(userId);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("old.email@example.com");
        existingUser.setPhoneNumber("+1234567890");

        when(userRepository.findById((long) userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        UserDTO updatedUser = userService.updateUserProfile(userId, newEmail, newPhoneNumber);

        // Assert
        assertEquals(newEmail, updatedUser.getEmail());
        assertEquals(newPhoneNumber, updatedUser.getPhoneNumber());
    }
}