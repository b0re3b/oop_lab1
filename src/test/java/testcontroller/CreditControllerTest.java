package testcontroller;

import com.creditapp.Controller.CreditController;
import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Service.CreditService;
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
public class CreditControllerTest {

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CreditController creditController;

    private List<CreditOffer> mockActiveCredits;
    private ActiveCredit mockActiveCredit;

    @BeforeEach
    void setUp() {
        mockActiveCredits = Arrays.asList(
                new CreditOffer(),
                new CreditOffer()
        );
        mockActiveCredit = new ActiveCredit();
    }

    @Test
    void testGetActiveCredits_ValidCustomerId() {
        Integer customerId = 1;
        when(creditService.getActiveCreditsForCustomer(customerId))
                .thenReturn(mockActiveCredits);

        ResponseEntity<List<CreditOffer>> response = creditController.getActiveCredits(customerId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockActiveCredits, response.getBody());
        verify(creditService).getActiveCreditsForCustomer(customerId);
    }

    @Test
    void testGetActiveCredits_NullCustomerId() {
        ResponseEntity<List<CreditOffer>> response = creditController.getActiveCredits(null);

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testApplyCreditOffer() {
        CreditController.CreditApplicationRequest request = new CreditController.CreditApplicationRequest();
        request.setOfferId(1);
        request.setApproved(true);

        when(creditService.processCreditApplication(request.getOfferId(), request.isApproved()))
                .thenReturn(mockActiveCredit);

        ResponseEntity<ActiveCredit> response = creditController.applyCreditOffer(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockActiveCredit, response.getBody());
        verify(creditService).processCreditApplication(request.getOfferId(), request.isApproved());
    }

    @Test
    void testProcessEarlyRepayment() {
        CreditController.EarlyRepaymentRequest request = new CreditController.EarlyRepaymentRequest();
        request.setOfferId(1);
        BigDecimal expectedRemainingAmount = BigDecimal.valueOf(1000);

        when(creditService.calculateEarlyRepayment(request.getOfferId()))
                .thenReturn(expectedRemainingAmount);

        ResponseEntity<BigDecimal> response = creditController.processEarlyRepayment(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedRemainingAmount, response.getBody());
        verify(creditService).calculateEarlyRepayment(request.getOfferId());
    }
}