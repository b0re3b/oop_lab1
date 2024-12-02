package testmodel;

import com.creditapp.Model.Credit;
import com.creditapp.Model.CreditOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

class CreditTest {

    private Credit credit;

    @BeforeEach
    void setUp() {
        credit = new Credit();
    }

    @Test
    void testCreditCreation() {
        credit.setTypeId(1L);
        credit.setTypeName("Personal Loan");
        credit.setMinAmount(BigDecimal.valueOf(1000));
        credit.setMaxAmount(BigDecimal.valueOf(50000));
        credit.setMinTermMonths(12);
        credit.setMaxTermMonths(60);

        assertEquals(1L, credit.getTypeId());
        assertEquals("Personal Loan", credit.getTypeName());
        assertEquals(BigDecimal.valueOf(1000), credit.getMinAmount());
        assertEquals(BigDecimal.valueOf(50000), credit.getMaxAmount());
        assertEquals(12, credit.getMinTermMonths());
        assertEquals(60, credit.getMaxTermMonths());
    }

    @Test
    void testCreditOffers() {
        CreditOffer offer1 = new CreditOffer();
        CreditOffer offer2 = new CreditOffer();
        credit.setCreditOffers(new ArrayList<>());
        credit.getCreditOffers().add(offer1);
        credit.getCreditOffers().add(offer2);

        assertEquals(2, credit.getCreditOffers().size());
    }

    @Test
    void testParameterizedConstructor() {
        Credit creditWithParams = new Credit(
                "Business Loan",
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(500000),
                24,
                120
        );

        assertEquals("Business Loan", creditWithParams.getTypeName());
        assertEquals(BigDecimal.valueOf(10000), creditWithParams.getMinAmount());
        assertEquals(BigDecimal.valueOf(500000), creditWithParams.getMaxAmount());
        assertEquals(24, creditWithParams.getMinTermMonths());
        assertEquals(120, creditWithParams.getMaxTermMonths());
    }
}