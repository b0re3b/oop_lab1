package testcontroller;

import com.creditapp.Controller.UserController;
import com.creditapp.DTO.UserDTO;
import com.creditapp.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        mockUserDTO = new UserDTO(1, "John", "Doe", "john@example.com", "1234567890");
        mockUserDTO.setCustomerId(1);
        mockUserDTO.setFirstName("John");
        mockUserDTO.setLastName("Doe");
        mockUserDTO.setEmail("john@example.com");
        mockUserDTO.setPhoneNumber("1234567890");
    }


    @Test
    void testRegisterUser_ValidInput() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john@example.com";
        String phoneNumber = "1234567890";
        String passportNumber = "AB123456";

        when(userService.registerNewUser(firstName, lastName, email, phoneNumber, passportNumber))
                .thenReturn(mockUserDTO);

        ResponseEntity<?> response = userController.registerUser(
                firstName, lastName, email, phoneNumber, passportNumber
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUserDTO, response.getBody());
        verify(userService).registerNewUser(firstName, lastName, email, phoneNumber, passportNumber);
    }

    @Test
    void testRegisterUser_InvalidInput() {
        ResponseEntity<?> response = userController.registerUser(
                "", "", "", "", ""
        );

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserController.ErrorResponse);
    }

    @Test
    void testGetUserProfile_ExistingUser() {
        int userId = 1;
        when(userService.getUserProfile(userId)).thenReturn(mockUserDTO);

        ResponseEntity<UserDTO> response = userController.getUserProfile(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUserDTO, response.getBody());
        verify(userService).getUserProfile(userId);
    }

    @Test
    void testGetUserProfile_NonExistingUser() {
        int userId = 999;
        when(userService.getUserProfile(userId)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<UserDTO> response = userController.getUserProfile(userId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateUserProfile_ValidInput() {
        int userId = 1;
        String email = "new@example.com";
        String phoneNumber = "9876543210";

        when(userService.updateUserProfile(userId, email, phoneNumber)).thenReturn(mockUserDTO);

        ResponseEntity<?> response = userController.updateUserProfile(userId, email, phoneNumber);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUserDTO, response.getBody());
        verify(userService).updateUserProfile(userId, email, phoneNumber);
    }

    @Test
    void testUpdateUserProfile_InvalidInput() {
        ResponseEntity<?> response = userController.updateUserProfile(1, "", "");

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserController.ErrorResponse);
    }

    @Test
    void testCalculateCreditRating() {
        BigDecimal monthlyIncome = BigDecimal.valueOf(5000);
        List<Long> existingCredits = Arrays.asList(1L, 2L);
        int expectedRating = 750;

        when(userService.calculateCreditRating(monthlyIncome, existingCredits))
                .thenReturn(expectedRating);

        ResponseEntity<Integer> response = userController.calculateCreditRating(monthlyIncome, existingCredits);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedRating, response.getBody());
        verify(userService).calculateCreditRating(monthlyIncome, existingCredits);
    }
}