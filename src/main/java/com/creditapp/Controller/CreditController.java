package com.creditapp.Controller;

import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Service.CreditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for handling credit-related operations such as applying for credit,
 * processing early repayments, and fetching active credits for customers.
 */
@RestController
@RequestMapping("/credits")
public class CreditController {

    private final CreditService creditService;

    /**
     * Constructor with dependency injection for CreditService.
     *
     * @param creditService The CreditService to be used by this controller.
     */
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    /**
     * Retrieves active credits for a customer.
     *
     * @param customerId The ID of the customer for whom active credits are to be fetched.
     * @return A list of active credit offers for the given customer.
     */
    @GetMapping("/active")
    public ResponseEntity<List<CreditOffer>> getActiveCredits(@RequestParam("customerId") Integer customerId) {
        if (customerId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<CreditOffer> activeCredits = creditService.getActiveCreditsForCustomer(customerId);
        return ResponseEntity.ok(activeCredits);
    }

    /**
     * Processes a credit application and creates an active credit.
     *
     * @param applicationRequest The credit application request data.
     * @return The created active credit.
     */
    @PostMapping("/apply")
    public ResponseEntity<ActiveCredit> applyCreditOffer(@RequestBody CreditApplicationRequest applicationRequest) {
        ActiveCredit creditOffer = creditService.processCreditApplication(
                applicationRequest.getOfferId(),
                applicationRequest.isApproved());
        return ResponseEntity.ok(creditOffer);
    }

    /**
     * Processes an early repayment request and calculates the remaining amount to be repaid.
     *
     * @param repaymentRequest The early repayment request data.
     * @return The remaining amount to be repaid after early repayment.
     */
    @PostMapping("/repayment")
    public ResponseEntity<BigDecimal> processEarlyRepayment(@RequestBody EarlyRepaymentRequest repaymentRequest) {
        BigDecimal remainingAmount = creditService.calculateEarlyRepayment(repaymentRequest.getOfferId());
        return ResponseEntity.ok(remainingAmount);
    }

    /**
     * Converts an object into JSON format.
     * This method is kept for specific cases where manual serialization is needed.
     *
     * @param object The object to be serialized to JSON.
     * @return A JSON string representation of the object.
     */
    private String convertObjectToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return "{\"error\": \"Error serializing object\"}";
        }
    }

    /**
     * DTO class for handling credit application requests.
     */
    public static class CreditApplicationRequest {
        private int offerId;
        private boolean approved;

        public int getOfferId() {
            return offerId;
        }

        public void setOfferId(int offerId) {
            this.offerId = offerId;
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }
    }

    /**
     * DTO class for handling early repayment requests.
     */
    public static class EarlyRepaymentRequest {
        private int offerId;

        public int getOfferId() {
            return offerId;
        }

        public void setOfferId(int offerId) {
            this.offerId = offerId;
        }
    }
}
