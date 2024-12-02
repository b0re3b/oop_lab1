package testmodel;

import com.creditapp.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Date;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        user.setCustomerId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(Date.valueOf("1990-01-01"));
        user.setPassportNumber("AB123456");
        user.setPhoneNumber("+1234567890");
        user.setEmail("john.doe@example.com");
        user.setMonthlyIncome(BigDecimal.valueOf(5000));
        user.setCreditRating(750);

        assertEquals(1, user.getCustomerId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(Date.valueOf("1990-01-01"), user.getBirthDate());
        assertEquals("AB123456", user.getPassportNumber());
        assertEquals("+1234567890", user.getPhoneNumber());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(BigDecimal.valueOf(5000), user.getMonthlyIncome());
        assertEquals(750, user.getCreditRating());
    }

    @Test
    void testUserEquality() {
        User user1 = new User();
        user1.setCustomerId(1);
        user1.setPassportNumber("AB123456");

        User user2 = new User();
        user2.setCustomerId(1);
        user2.setPassportNumber("AB123456");

        User user3 = new User();
        user3.setCustomerId(2);
        user3.setPassportNumber("CD789012");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testUserToString() {
        user.setCustomerId(1);
        user.setFirstName("Jane");
        user.setLastName("Smith");

        String expectedString = "User{customerId=1, firstName='Jane', lastName='Smith', birthDate=null, passportNumber='null', phoneNumber='null', email='null', monthlyIncome=null, creditRating=null}";
        assertEquals(expectedString, user.toString());
    }
}