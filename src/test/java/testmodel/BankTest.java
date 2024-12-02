package testmodel;

import com.creditapp.Model.Bank;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void testBankCreation() {
        bank.setBankId(1L);
        bank.setBankName("Example Bank");
        bank.setBankCode("EXMPL");
        bank.setContactPhone("+1234567890");
        bank.setWebsite("www.examplebank.com");

        assertEquals(1L, bank.getBankId());
        assertEquals("Example Bank", bank.getBankName());
        assertEquals("EXMPL", bank.getBankCode());
        assertEquals("+1234567890", bank.getContactPhone());
        assertEquals("www.examplebank.com", bank.getWebsite());
    }

    @Test
    void testCreditOffersAndCustomers() {
        CreditOffer offer1 = new CreditOffer();
        CreditOffer offer2 = new CreditOffer();
        User customer1 = new User();
        User customer2 = new User();

        bank.setCreditOffers(Arrays.asList(offer1, offer2));
        bank.setUsers(Arrays.asList(customer1, customer2));

        List<CreditOffer> creditOffers = bank.getCreditOffers();
        List<User> customers = bank.getUsers();

        assertEquals(2, creditOffers.size());
        assertEquals(2, customers.size());
        assertTrue(creditOffers.contains(offer1));
        assertTrue(creditOffers.contains(offer2));
        assertTrue(customers.contains(customer1));
        assertTrue(customers.contains(customer2));
    }
}