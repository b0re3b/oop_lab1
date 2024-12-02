package testmodel;

import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.Bank;
import com.creditapp.Model.Credit;
import com.creditapp.Model.CreditOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class CreditOfferTest {

    private CreditOffer creditOffer;

    @BeforeEach
    void setUp() {
        creditOffer = new CreditOffer();
    }

    @Test
    void testCreditOfferCreation() {
        Bank bank = new Bank();
        Credit creditType = new Credit();

        creditOffer.setOfferId(1L);
        creditOffer.setBank(bank);
        creditOffer.setCreditType(creditType);
        creditOffer.setInterestRate(BigDecimal.valueOf(5.5));
        creditOffer.setEarlyRepaymentAllowed(true);
        creditOffer.setCreditLineIncreaseAllowed(false);
        creditOffer.setDescription("Flexible personal loan");

        assertEquals(1L, creditOffer.getOfferId());
        assertEquals(bank, creditOffer.getBank());
        assertEquals(creditType, creditOffer.getCreditType());
        assertEquals(BigDecimal.valueOf(5.5), creditOffer.getInterestRate());
        assertTrue(creditOffer.getEarlyRepaymentAllowed());
        assertFalse(creditOffer.getCreditLineIncreaseAllowed());
        assertEquals("Flexible personal loan", creditOffer.getDescription());
    }

    @Test
    void testActiveCreditManagement() {
        ActiveCredit activeCredit1 = new ActiveCredit();
        ActiveCredit activeCredit2 = new ActiveCredit();

        creditOffer.addActiveCredit(activeCredit1);
        creditOffer.addActiveCredit(activeCredit2);

        assertEquals(2, creditOffer.getActiveCredits().size());
        assertEquals(creditOffer, activeCredit1.getCreditOffer());
        assertEquals(creditOffer, activeCredit2.getCreditOffer());

        creditOffer.removeActiveCredit(activeCredit1);
        assertEquals(1, creditOffer.getActiveCredits().size());
        assertNull(activeCredit1.getCreditOffer());
    }
}