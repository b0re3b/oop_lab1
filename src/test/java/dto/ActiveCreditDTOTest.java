package dto;

import com.creditapp.DTO.ActiveCreditDTO;
import com.creditapp.DTO.CreditOfferDTO;
import com.creditapp.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ActiveCreditDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidActiveCreditDTO() {
        ActiveCreditDTO dto = new ActiveCreditDTO(
                1L,
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(500),
                LocalDate.now(),
                LocalDate.now().plusMonths(12),
                ActiveCreditDTO.Status.APPROVED
        );

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testInvalidLoanAmount() {
        ActiveCreditDTO dto = new ActiveCreditDTO(
                1L,
                BigDecimal.ZERO,
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(500),
                LocalDate.now(),
                LocalDate.now().plusMonths(12),
                ActiveCreditDTO.Status.APPROVED
        );

        assertFalse(validator.validate(dto).isEmpty());
    }
}

class CreditOfferDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCreditOfferDTO() {
        CreditOfferDTO dto = new CreditOfferDTO(
                1L,
                "PrivatBank",
                "Personal",
                BigDecimal.valueOf(12.5),
                12,
                60,
                true
        );

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testCalculateMonthlyPayment() {
        CreditOfferDTO dto = new CreditOfferDTO(
                1L,
                "PrivatBank",
                "Personal",
                BigDecimal.valueOf(12.0),
                12,
                60,
                true
        );

        BigDecimal amount = BigDecimal.valueOf(10000);
        int term = 24;
        BigDecimal monthlyPayment = dto.calculateMonthlyPayment(amount, term);

        assertNotNull(monthlyPayment);
        assertTrue(monthlyPayment.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testInvalidTerm() {
        CreditOfferDTO dto = new CreditOfferDTO(
                1L,
                "PrivatBank",
                "Personal",
                BigDecimal.valueOf(12.5),
                12,
                60,
                true
        );

        assertThrows(IllegalArgumentException.class, () ->
                dto.calculateMonthlyPayment(BigDecimal.valueOf(10000), 11)
        );
    }
}

class UserDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDTO() {
        UserDTO dto = new UserDTO(
                1,
                "John",
                "Doe",
                "john.doe@example.com",
                "+380123456789"
        );
        dto.setMonthlyIncome(BigDecimal.valueOf(5000));
        dto.setCreditRating(750);

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testInvalidEmail() {
        UserDTO dto = new UserDTO(
                1,
                "John",
                "Doe",
                "invalid-email",
                "+380123456789"
        );

        assertFalse(validator.validate(dto).isEmpty());
    }
}