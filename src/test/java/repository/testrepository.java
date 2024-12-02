package repository;

import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.Bank;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Model.User;
import com.creditapp.Repository.CreditRepository;
import com.creditapp.Repository.CreditRepositoryImpl;
import com.creditapp.Repository.UserRepository;
import com.creditapp.Service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CreditService creditService;

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditRepositoryImpl creditRepositoryImpl;

    private CreditOffer testCreditOffer;
    private Bank testBank;
    private ActiveCredit testActiveCredit;

    @BeforeEach
    public void setUp() {
        testBank = new Bank();
        testBank.setBankId(1L);
        testBank.setBankName("Test Bank");

        testCreditOffer = new CreditOffer();
        testCreditOffer.setOfferId(1L);
        testCreditOffer.setInterestRate(BigDecimal.valueOf(50));
        testCreditOffer.setBank(testBank);

        testActiveCredit = new ActiveCredit();
        testActiveCredit.setActiveCreditId(1L);
        testActiveCredit.setCreditOffer(testCreditOffer);
    }

    @Test
    public void testSaveActiveCredit_NewCredit() {
        when(entityManager.find(ActiveCredit.class, testActiveCredit.getActiveCreditId())).thenReturn(null);

        ActiveCredit savedCredit = creditRepositoryImpl.saveActiveCredit(testActiveCredit);

        verify(entityManager).persist(testActiveCredit);
        assertEquals(testActiveCredit, savedCredit);
    }

    @Test
    public void testSaveActiveCredit_ExistingCredit() {
        ActiveCredit existingCredit = new ActiveCredit();
        existingCredit.setActiveCreditId(1L);

        when(entityManager.find(ActiveCredit.class, testActiveCredit.getActiveCreditId())).thenReturn(existingCredit);

        ActiveCredit updatedCredit = creditRepositoryImpl.saveActiveCredit(testActiveCredit);

        assertEquals(existingCredit, updatedCredit);
        assertEquals(testActiveCredit.getCreditOffer(), existingCredit.getCreditOffer());
    }

    @Test
    public void testSaveBank_NewBank() {
        when(entityManager.find(Bank.class, testBank.getBankId())).thenReturn(null);

        Bank savedBank = creditRepositoryImpl.saveBank(testBank);

        verify(entityManager).persist(testBank);
        assertEquals(testBank, savedBank);
    }

    @Test
    public void testSaveBank_ExistingBank() {
        Bank existingBank = new Bank();
        existingBank.setBankId(1L);

        when(entityManager.find(Bank.class, testBank.getBankId())).thenReturn(existingBank);

        Bank updatedBank = creditRepositoryImpl.saveBank(testBank);

        assertEquals(existingBank, updatedBank);
        assertEquals(testBank.getBankName(), existingBank.getBankName());
    }

    @Test
    public void testGetCreditOfferById() {
        when(creditRepository.findById(1L)).thenReturn(Optional.of(testCreditOffer));

        Optional<CreditOffer> foundOffer = creditRepositoryImpl.getCreditOfferById(1L);

        assertTrue(foundOffer.isPresent());
        assertEquals(testCreditOffer, foundOffer.get());
    }

    @Test
    public void testGetAllCreditOffers() {
        List<CreditOffer> mockOffers = List.of(testCreditOffer);
        when(creditRepository.findAll()).thenReturn(mockOffers);

        List<CreditOffer> allOffers = creditRepositoryImpl.getAllCreditOffers();

        assertEquals(1, allOffers.size());
        assertEquals(testCreditOffer, allOffers.get(0));
    }

    @Test
    public void testDeleteCreditOffer_Exists() {
        when(creditRepositoryImpl.getCreditOfferById(1L)).thenReturn(Optional.of(testCreditOffer));

        boolean deleted = creditRepositoryImpl.deleteCreditOffer(1L);

        assertTrue(deleted);
        verify(creditService).deleteCreditOffer(1L);
    }

    @Test
    public void testDeleteCreditOffer_NotExists() {
        when(creditRepositoryImpl.getCreditOfferById(1L)).thenReturn(Optional.empty());

        boolean deleted = creditRepositoryImpl.deleteCreditOffer(1L);

        assertFalse(deleted);
        verify(creditService, never()).deleteCreditOffer(anyLong());
    }
}

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setCustomerId(Math.toIntExact(1L));
        testUser.setEmail("test@example.com");
        testUser.setLastName("Doe");
        testUser.setPhoneNumber("1234567890");
    }

    @Test
    public void testFindByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(testUser, foundUser.get());
    }

    @Test
    public void testFindByLastName() {
        List<User> mockUsers = List.of(testUser);
        when(userRepository.findByLastName("Doe")).thenReturn(mockUsers);

        List<User> foundUsers = userRepository.findByLastName("Doe");

        assertEquals(1, foundUsers.size());
        assertEquals(testUser, foundUsers.get(0));
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    public void testFindUsersWithCreditApplications() {
        List<User> mockUsers = List.of(testUser);
        when(userRepository.findUsersWithCreditApplications()).thenReturn(mockUsers);

        List<User> usersWithApplications = userRepository.findUsersWithCreditApplications();

        assertFalse(usersWithApplications.isEmpty());
        assertEquals(testUser, usersWithApplications.get(0));
    }
}