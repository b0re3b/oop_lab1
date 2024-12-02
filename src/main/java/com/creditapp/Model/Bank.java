package com.creditapp.Model;

import com.creditapp.DTO.ActiveCreditDTO;
import com.creditapp.Repository.CreditRepositoryImpl;
import com.creditapp.Service.CreditService;
import com.creditapp.Repository.CreditRepository;
import com.creditapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a bank in the credit application system.
 * The Bank entity contains bank details and manages relationships with credit offers and customers.
 */
@Entity
@Table(name = "banks")
@Component
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long bank_id;

    @Column(name = "bank_name")
    private String bank_name;

    @Column(name = "bank_code", unique = true)
    private String bank_code;

    @Column(name = "contact_phone")
    private String contact_phone;

    @Column(name = "website")
    private String website;

    @OneToMany(mappedBy = "bank")
    private List<CreditOffer> credit_offers;

    @OneToMany(mappedBy = "bank")
    private List<User> customers;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditOffer> creditOffers = new ArrayList<>();

    // Spring Autowired Services and Repositories
    @Autowired
    private CreditService creditService;

    @Autowired
    private CreditRepositoryImpl impl;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a list of active credit offers for a specific customer based on their customer ID.
     *
     * @param customerId The customer's ID.
     * @return List of CreditOffer objects associated with the customer.
     */
    public List<CreditOffer> getUserCreditOffers(Integer customerId) {
        return creditService.getActiveCreditOffers(customerId);
    }

    /**
     * Retrieves a list of credit offers based on their status.
     *
     * @param status The status of the credit offers (e.g., PENDING, APPROVED).
     * @return List of CreditOffer objects that match the given status.
     */
    public List<CreditOffer> getCreditOffersByStatus(ActiveCreditDTO.Status status) {
        return creditService.getCreditOffersByStatus(status);
    }

    /**
     * Retrieves a credit offer by its unique ID.
     *
     * @param creditOfferId The ID of the credit offer.
     * @return An Optional containing the CreditOffer if found, otherwise empty.
     */
    public Optional<CreditOffer> getCreditOfferById(long creditOfferId) {
        return impl.getCreditOfferById(creditOfferId);
    }

    // Getters and Setters for the Bank entity fields
    public Long getBankId() {
        return bank_id;
    }

    public void setBankId(Long bankId) {
        this.bank_id = bankId;
    }

    public String getBankName() {
        return bank_name;
    }

    public void setBankName(String bankName) {
        this.bank_name = bankName;
    }

    public String getBankCode() {
        return bank_code;
    }

    public void setBankCode(String bankCode) {
        this.bank_code = bankCode;
    }

    public String getContactPhone() {
        return contact_phone;
    }

    public void setContactPhone(String contactPhone) {
        this.contact_phone = contactPhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<CreditOffer> getCreditOffers() {
        return credit_offers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.credit_offers = creditOffers;
    }

    public List<User> getUsers() {
        return customers;
    }

    public void setUsers(List<User> users) {
        this.customers = users;
    }

    /**
     * Provides a string representation of the Bank entity.
     * This includes the bank's details like name, code, contact phone, and website.
     *
     * @return A string representation of the Bank object.
     */
    @Override
    public String toString() {
        return "Bank{" +
                "bankId=" + bank_id +
                ", bankName='" + bank_name + '\'' +
                ", bankCode='" + bank_code + '\'' +
                ", contactPhone='" + contact_phone + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
