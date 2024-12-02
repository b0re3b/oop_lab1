package testservice;

import com.creditapp.DTO.ActiveCreditDTO;
import com.creditapp.Model.*;
import com.creditapp.Repository.CreditRepository;
import com.creditapp.Repository.CreditRepositoryImpl;
import com.creditapp.Repository.UserRepository;
import com.creditapp.Service.CreditService;
import com.creditapp.Service.UserService;
import com.creditapp.util.CreditCalculator;
import com.creditapp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreditServiceTest {

    @Mock
    private ActiveCredit activeCredit;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private UserService userService;

    @Mock
    private CreditCalculator creditCalculator;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Credit credit;

    @Mock
    private CreditRepositoryImpl implement;

    @InjectMocks
    private CreditService creditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCreditOffer_ValidParameters_ReturnsCreatedOffer() {
        // Arrange
        Long bankId = 1L;
        long typeId = 1L;
        BigDecimal amount = BigDecimal.valueOf(10000);
        int termMonths = 36;

        Bank bank = new Bank();
        bank.setBankId(bankId);

        Credit creditType = new Credit();
        creditType.setTypeId(typeId);

        when(creditRepository.findBankById(bankId)).thenReturn(Optional.of(bank));
        when(userRepository.findCreditTypeById(typeId)).thenReturn(Optional.of(creditType));
        when(creditCalculator.calculateMonthlyPayment(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(300));
        when(creditRepository.save(any(CreditOffer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditOffer result = creditService.createCreditOffer(bankId, typeId, amount, termMonths);

        // Assert
        assertNotNull(result);
        assertEquals(bank, result.getBank());
        assertEquals(creditType, result.getCreditType());
        assertNotNull(result.getActiveCredits());
        assertFalse(result.getActiveCredits().isEmpty());

        verify(creditRepository).save(any(CreditOffer.class));
    }

    @Test
    void processCreditApplication_ApprovedFlow() {
        // Arrange
        long offerId = 1L;
        CreditOffer creditOffer = new CreditOffer();
        ActiveCredit activeCreditInstance = new ActiveCredit();
        activeCreditInstance.setStatus(ActiveCredit.Status.PENDING);
        creditOffer.addActiveCredit(activeCreditInstance);

        when(creditRepository.findById(offerId)).thenReturn(Optional.of(creditOffer));
        when(implement.saveActiveCredit(any(ActiveCredit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ActiveCredit result = creditService.processCreditApplication(offerId, true);

        // Assert
        assertEquals(ActiveCredit.Status.APPROVED, result.getStatus());
        verify(implement).saveActiveCredit(any(ActiveCredit.class));
    }

    @Test
    void calculateEarlyRepayment_ApprovedCredit_CalculatesCorrectly() {
        // Arrange
        long offerId = 1L;
        CreditOffer creditOffer = new CreditOffer();
        creditOffer.setInterestRate(BigDecimal.valueOf(5.0));

        ActiveCredit activeCreditInstance = new ActiveCredit();
        activeCreditInstance.setStatus(ActiveCredit.Status.APPROVED);
        activeCreditInstance.setRemainingBalance(BigDecimal.valueOf(8000));
        activeCreditInstance.setLoanTermMonths(36);
        creditOffer.addActiveCredit(activeCreditInstance);

        when(creditRepository.findById(offerId)).thenReturn(Optional.of(creditOffer));

        // Act
        BigDecimal earlyRepaymentAmount = creditService.calculateEarlyRepayment(offerId);

        // Assert
        assertNotNull(earlyRepaymentAmount);
        assertTrue(earlyRepaymentAmount.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void getActiveCreditsForCustomer_ReturnsOnlyApprovedCredits() {
        // Arrange
        long customerId = 1L;
        CreditOffer approvedOffer = createMockCreditOffer(ActiveCredit.Status.APPROVED);
        CreditOffer pendingOffer = createMockCreditOffer(ActiveCredit.Status.PENDING);

        when(creditRepository.findCreditOffersByCustomerId(customerId))
                .thenReturn(Arrays.asList(approvedOffer, pendingOffer));

        // Act
        List<CreditOffer> result = creditService.getActiveCreditsForCustomer(customerId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(ActiveCredit.Status.APPROVED,
                result.get(0).getActiveCredits().iterator().next().getStatus());
    }

    private CreditOffer createMockCreditOffer(ActiveCredit.Status status) {
        CreditOffer offer = new CreditOffer();
        ActiveCredit activeCredit = new ActiveCredit();
        activeCredit.setStatus(status);
        offer.addActiveCredit(activeCredit);
        return offer;
    }
}