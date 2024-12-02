package com.creditapp.util;
import com.creditapp.Model.User;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;


@Component
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$"
    );

    public boolean isValidUser(User user) {
        if (user == null) {
            return false;
        }

        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            return false;
        }

        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            return false;
        }

        if (!isValidEmail(user.getEmail())) {
            return false;
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            return false;
        }

        return true;
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }

    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        return password.length() >= 8 &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
    }

    public boolean isValidCreditScore(int creditScore) {
        return creditScore >= 300 && creditScore <= 850;
    }

    public boolean isValidIncome(double income) {
        return income > 0;
    }


    public boolean validateCreditAmount(double amount, double minAmount, double maxAmount) {
        return amount >= minAmount && amount <= maxAmount;
    }


    public boolean validateCreditTerm(int termMonths, int minTerm, int maxTerm) {
        return termMonths >= minTerm && termMonths <= maxTerm;
    }
}
