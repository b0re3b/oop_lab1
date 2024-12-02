import com.creditapp.CreditApplicationMain;
import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Repository.CreditRepositoryImpl;
import com.creditapp.Service.CreditService;
import com.creditapp.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class testmain {

    @Mock
    private CreditRepositoryImpl mockRepository;

    @Mock
    private CreditService mockCreditService;

    @Mock
    private AppProperties mockAppProperties;

    private CreditApplicationMain creditApplicationMain;

    @BeforeEach
    public void setUp() {
        creditApplicationMain = new CreditApplicationMain(mockRepository, mockCreditService, mockAppProperties);
    }

    @Test
    public void testCreateCreditOffer_Success() throws Exception {
        // Arrange
        CreditOffer expectedOffer = new CreditOffer();
        expectedOffer.setOfferId(1L);
        expectedOffer.setInterestRate(BigDecimal.valueOf(5.5));

        when(mockCreditService.createCreditOffer(anyLong(), anyLong(), any(BigDecimal.class), anyInt()))
                .thenReturn(expectedOffer);

        // Act
        CreditOffer resultOffer = mockCreditService.createCreditOffer(1L, 2L, BigDecimal.valueOf(10000), 36);

        // Assert
        assertNotNull(resultOffer);
        assertEquals(1L, resultOffer.getOfferId());
        assertEquals(5.5, resultOffer.getInterestRate());

        verify(mockCreditService).createCreditOffer(1L, 2L, BigDecimal.valueOf(10000), 36);
    }

    @Test
    public void testFindSuitableCreditOffers_MultipleOffers() throws Exception {
        // Arrange
        CreditOffer offer1 = new CreditOffer();
        offer1.setOfferId(1L);
        offer1.setInterestRate(BigDecimal.valueOf(4.5));

        CreditOffer offer2 = new CreditOffer();
        offer2.setOfferId(2L);
        offer2.setInterestRate(BigDecimal.valueOf(5.0));

        List<CreditOffer> expectedOffers = Arrays.asList(offer1, offer2);

        when(mockCreditService.findSuitableCreditOffers(anyLong(), any(BigDecimal.class), anyInt()))
                .thenReturn(expectedOffers);

        // Act
        List<CreditOffer> resultOffers = mockCreditService.findSuitableCreditOffers(100L, BigDecimal.valueOf(15000), 48);

        // Assert
        assertNotNull(resultOffers);
        assertEquals(2, resultOffers.size());
        assertEquals(1L, resultOffers.get(0).getOfferId());
        assertEquals(2L, resultOffers.get(1).getOfferId());

        verify(mockCreditService).findSuitableCreditOffers(100L, BigDecimal.valueOf(15000), 48);
    }

    @Test
    public void testProcessCreditApplication_Approved() throws Exception {
        // Arrange
        ActiveCredit activeCredit = new ActiveCredit();
        activeCredit.setStatus(ActiveCredit.Status.valueOf("APPROVED"));

        when(mockCreditService.processCreditApplication(anyLong(), anyBoolean()))
                .thenReturn(activeCredit);

        // Act
        ActiveCredit resultCredit = mockCreditService.processCreditApplication(5L, true);

        // Assert
        assertNotNull(resultCredit);
        assertEquals("APPROVED", resultCredit.getStatus());

        verify(mockCreditService).processCreditApplication(5L, true);
    }

    @Test
    public void testCalculateEarlyRepayment() throws Exception {
        // Arrange
        BigDecimal expectedRepaymentAmount = BigDecimal.valueOf(9500.50);

        when(mockCreditService.calculateEarlyRepayment(anyLong()))
                .thenReturn(expectedRepaymentAmount);

        // Act
        BigDecimal resultAmount = mockCreditService.calculateEarlyRepayment(3L);

        // Assert
        assertNotNull(resultAmount);
        assertEquals(expectedRepaymentAmount, resultAmount);

        verify(mockCreditService).calculateEarlyRepayment(3L);
    }

    @Test
    public void testAppProperties() {
        // Arrange
        when(mockAppProperties.getProperty("db.url")).thenReturn("jdbc:postgresql://localhost:5433/banking");
        when(mockAppProperties.getIntProperty("db.pool.max-size", 10)).thenReturn(15);
        when(mockAppProperties.getBooleanProperty("app.logging.enabled", true)).thenReturn(true);

        // Act & Assert
        assertEquals("jdbc:postgresql://localhost:5433/banking", mockAppProperties.getProperty("db.url"));
        assertEquals(15, mockAppProperties.getIntProperty("db.pool.max-size", 10));
        assertTrue(mockAppProperties.getBooleanProperty("app.logging.enabled", true));
    }
}