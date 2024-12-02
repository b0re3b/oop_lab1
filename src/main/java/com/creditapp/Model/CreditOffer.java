package com.creditapp.Model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас, що представляє кредитну пропозицію.
 * Він зв'язується з таблицею credit_offers у базі даних.
 */
@Entity
@Table(name = "credit_offers") // Зв'язок з таблицею credit_offers
public class CreditOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Генерація ID
    @Column(name = "offer_id") // Назва стовпця в базі даних
    private Long offerId;

    /**
     * Зв'язок з банком, який пропонує кредит.
     * Зовнішній ключ до таблиці banks.
     */
    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false) // Зовнішній ключ до таблиці banks
    private Bank bank;

    /**
     * Зв'язок з типом кредиту.
     * Зовнішній ключ до таблиці credit_types.
     */
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false) // Зовнішній ключ до таблиці credit_types
    private Credit creditType;

    @Column(name = "interest_rate", nullable = false) // Процентна ставка
    private BigDecimal interestRate;

    @Column(name = "early_repayment_allowed", nullable = false) // Дозволено дострокове погашення
    private Boolean earlyRepaymentAllowed;

    @Column(name = "credit_line_increase_allowed", nullable = false) // Дозволено збільшення лінії кредиту
    private Boolean creditLineIncreaseAllowed;

    @Column(name = "description") // Опис кредитної пропозиції
    private String description;

    /**
     * Зв'язок з активними кредитами.
     * Один до багатьох: одна кредитна пропозиція може мати багато активних кредитів.
     */
    @OneToMany(mappedBy = "creditOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActiveCredit> activeCredits = new ArrayList<>();

    // Конструктор за замовчуванням
    public CreditOffer() {}

    // Геттери та сеттери
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Credit getCreditType() {
        return creditType;
    }

    public void setCreditType(Credit creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Boolean getEarlyRepaymentAllowed() {
        return earlyRepaymentAllowed;
    }

    public void setEarlyRepaymentAllowed(Boolean earlyRepaymentAllowed) {
        this.earlyRepaymentAllowed = earlyRepaymentAllowed;
    }

    public Boolean getCreditLineIncreaseAllowed() {
        return creditLineIncreaseAllowed;
    }

    public void setCreditLineIncreaseAllowed(Boolean creditLineIncreaseAllowed) {
        this.creditLineIncreaseAllowed = creditLineIncreaseAllowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ActiveCredit> getActiveCredits() {
        return activeCredits;
    }

    /**
     * Додає активний кредит до списку.
     * @param activeCredit Активний кредит, який додається до пропозиції.
     */
    public void addActiveCredit(ActiveCredit activeCredit) {
        activeCredits.add(activeCredit);
        activeCredit.setCreditOffer(this);
    }

    /**
     * Видаляє активний кредит зі списку.
     * @param activeCredit Активний кредит, який видаляється з пропозиції.
     */
    public void removeActiveCredit(ActiveCredit activeCredit) {
        activeCredits.remove(activeCredit);
        activeCredit.setCreditOffer(null);
    }

    @Override
    public String toString() {
        return "CreditOffer{" +
                "offerId=" + offerId +
                ", interestRate=" + interestRate +
                ", earlyRepaymentAllowed=" + earlyRepaymentAllowed +
                ", creditLineIncreaseAllowed=" + creditLineIncreaseAllowed +
                ", description='" + description + '\'' +
                '}';
    }
}
