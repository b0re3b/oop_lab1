package testmodel;

import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class ActiveCreditTest {

    private ActiveCredit activeCredit;

    @BeforeEach
    void setUp() {
        activeCredit = new ActiveCredit();
    }

    @Test
    void testActiveCreditCreation() {
        User customer = new User();
        CreditOffer creditOffer = new CreditOffer();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(12);

        activeCredit.setActiveCreditId(1L);
        activeCredit.setCustomer(customer);
        activeCredit.setCreditOffer(creditOffer);
        activeCredit.setLoanAmount(BigDecimal.valueOf(10000));
        activeCredit.setLoanTermMonths(12);
        activeCredit.setStartDate(startDate);
        activeCredit.setEndDate(endDate);
        activeCredit.setMonthlyPayment(BigDecimal.valueOf(900));
        activeCredit.setRemainingBalance(BigDecimal.valueOf(10000));
        activeCredit.setStatus(ActiveCredit.Status.APPROVED);

        assertEquals(1L, activeCredit.getActiveCreditId());
        assertEquals(customer, activeCredit.getCustomer());
        assertEquals(creditOffer, activeCredit.getCreditOffer());
        assertEquals(BigDecimal.valueOf(10000), activeCredit.getLoanAmount());
        assertEquals(12, activeCredit.getLoanTermMonths());
        assertEquals(startDate, activeCredit.getStartDate());
        assertEquals(endDate, activeCredit.getEndDate());
        assertEquals(BigDecimal.valueOf(900), activeCredit.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(10000), activeCredit.getRemainingBalance());
        assertEquals(ActiveCredit.Status.APPROVED, activeCredit.getStatus());
    }

    @Test
    void testStatusEnumeration() {
        ActiveCredit.Status[] expectedStatuses = {
                ActiveCredit.Status.PENDING,
                ActiveCredit.Status.APPROVED,
                ActiveCredit.Status.REJECTED,
                ActiveCredit.Status.PROCESSED
        };

        assertEquals(4, expectedStatuses.length);
        assertNotNull(ActiveCredit.Status.valueOf("PENDING"));
        assertNotNull(ActiveCredit.Status.valueOf("APPROVED"));
        assertNotNull(ActiveCredit.Status.valueOf("REJECTED"));
        assertNotNull(ActiveCredit.Status.valueOf("PROCESSED"));
    }
}