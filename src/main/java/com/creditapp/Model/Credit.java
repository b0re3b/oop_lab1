package com.creditapp.Model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a credit type in the credit application system.
 * The Credit entity contains details about different credit types, including limits and terms.
 */
@Entity
@Table(name = "credit_types") // Mapping this class to the "credit_types" table in the database
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID generation strategy
    @Column(name = "type_id") // Column name in the database
    private Long type_id;

    @Column(name = "type_name") // Column for the name of the credit type
    private String type_name;

    @Column(name = "min_amount") // Minimum loan amount for this credit type
    private BigDecimal min_amount;

    @Column(name = "max_amount") // Maximum loan amount for this credit type
    private BigDecimal max_amount;

    @Column(name = "min_term_months") // Minimum term (in months) for this credit type
    private Integer min_term_months;

    @Column(name = "max_term_months") // Maximum term (in months) for this credit type
    private Integer max_term_months;

    // One-to-many relationship: One credit type can be associated with multiple credit offers
    @OneToMany(mappedBy = "credit_type")
    private List<CreditOffer> creditOffers;

    // Default constructor
    public Credit() {}

    /**
     * Parameterized constructor for easy initialization of a Credit entity.
     *
     * @param typeName     Name of the credit type
     * @param minAmount    Minimum loan amount
     * @param maxAmount    Maximum loan amount
     * @param minTermMonths Minimum loan term in months
     * @param maxTermMonths Maximum loan term in months
     */
    public Credit(String typeName, BigDecimal minAmount, BigDecimal maxAmount, Integer minTermMonths, Integer maxTermMonths) {
        this.type_name = typeName;
        this.min_amount = minAmount;
        this.max_amount = maxAmount;
        this.min_term_months = minTermMonths;
        this.max_term_months = maxTermMonths;
    }

    // Getters and Setters for the fields
    public Long getTypeId() {
        return type_id;
    }

    public void setTypeId(Long typeId) {
        this.type_id = typeId;
    }

    public String getTypeName() {
        return type_name;
    }

    public void setTypeName(String typeName) {
        this.type_name = typeName;
    }

    public BigDecimal getMinAmount() {
        return min_amount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.min_amount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return max_amount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.max_amount = maxAmount;
    }

    public Integer getMinTermMonths() {
        return min_term_months;
    }

    public void setMinTermMonths(Integer minTermMonths) {
        this.min_term_months = minTermMonths;
    }

    public Integer getMaxTermMonths() {
        return max_term_months;
    }

    public void setMaxTermMonths(Integer maxTermMonths) {
        this.max_term_months = maxTermMonths;
    }

    public List<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.creditOffers = creditOffers;
    }

    /**
     * Overridden toString method for better readability when printing the Credit object.
     *
     * @return A string representation of the Credit entity.
     */
    @Override
    public String toString() {
        return "Credit{" +
                "typeId=" + type_id +
                ", typeName='" + type_name + '\'' +
                ", minAmount=" + min_amount +
                ", maxAmount=" + max_amount +
                ", minTermMonths=" + min_term_months +
                ", maxTermMonths=" + max_term_months +
                '}';
    }
}
