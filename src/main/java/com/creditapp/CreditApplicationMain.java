package com.creditapp;

import com.creditapp.Model.ActiveCredit;
import com.creditapp.Model.CreditOffer;
import com.creditapp.Repository.CreditRepositoryImpl;
import com.creditapp.Service.CreditService;
import com.creditapp.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Credit Application System.
 * Provides an interactive console for managing credit operations such as creating credit offers,
 * processing applications, and viewing active credits.
 */
@EntityScan(basePackages = "com.creditapp.Model") // Scans for JPA entity classes
@SpringBootApplication(scanBasePackages = "com.creditapp") // Configures Spring Boot application
@EnableJpaRepositories(basePackages = "com.creditapp.Repository") // Enables JPA repositories
public class CreditApplicationMain {

    private final CreditRepositoryImpl impl;
    private final CreditService creditService;
    private final AppProperties appProperties;

    /**
     * Constructor for dependency injection.
     *
     * @param impl               Implementation of CreditRepository for custom logic.
     * @param creditService      Service to manage credit-related operations.
     * @param applicationProperties Configuration properties for the application.
     */
    @Autowired
    public CreditApplicationMain(CreditRepositoryImpl impl, CreditService creditService, AppProperties applicationProperties) {
        this.impl = impl;
        this.creditService = creditService;
        this.appProperties = applicationProperties;
    }

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        var context = SpringApplication.run(CreditApplicationMain.class, args);

        CreditApplicationMain app = context.getBean(CreditApplicationMain.class);
        app.runInteractiveConsole(); // Launch interactive console
    }

    /**
     * Runs the interactive console for user interaction.
     */
    private void runInteractiveConsole() {
        printAppProperties(); // Display application properties

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("\n--- Credit Application System ---");
                System.out.println("1. Create Credit Offer");
                System.out.println("2. Find Suitable Credit Offers");
                System.out.println("3. Process Credit Application");
                System.out.println("4. View Active Credits");
                System.out.println("5. Calculate Early Repayment");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> createCreditOffer(scanner);
                    case 2 -> findSuitableCreditOffers(scanner);
                    case 3 -> processCreditApplication(scanner);
                    case 4 -> viewActiveCredits(scanner);
                    case 5 -> calculateEarlyRepayment(scanner);
                    case 6 -> {
                        System.out.println("Exiting the application...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    /**
     * Prints application properties like database URL and logging settings.
     */
    private void printAppProperties() {
        System.out.println("Database URL: " + appProperties.getProperty("db.url"));
        System.out.println("Max Pool Size: " + appProperties.getIntProperty("db.pool.max-size", 10));
        System.out.println("Logging Enabled: " + appProperties.getBooleanProperty("app.logging.enabled", true));
    }

    /**
     * Handles user input to create a credit offer.
     *
     * @param scanner Scanner for user input.
     */
    private void createCreditOffer(Scanner scanner) {
        System.out.print("Enter Bank ID: ");
        long bankId = scanner.nextLong();

        System.out.print("Enter Credit Type ID: ");
        long typeId = scanner.nextLong();

        System.out.print("Enter Credit Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();

        System.out.print("Enter Term (months): ");
        int termMonths = scanner.nextInt();

        try {
            CreditOffer creditOffer = creditService.createCreditOffer(bankId, typeId, amount, termMonths);
            System.out.println("Credit Offer Created Successfully:");
            System.out.println("Offer ID: " + creditOffer.getOfferId());
            System.out.println("Interest Rate: " + creditOffer.getInterestRate() + "%");
        } catch (Exception e) {
            System.out.println("Failed to create credit offer: " + e.getMessage());
        }
    }

    /**
     * Handles user input to find suitable credit offers.
     *
     * @param scanner Scanner for user input.
     */
    private void findSuitableCreditOffers(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        long customerId = scanner.nextLong();

        System.out.print("Enter Desired Credit Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();

        System.out.print("Enter Desired Term (months): ");
        int termMonths = scanner.nextInt();

        try {
            List<CreditOffer> offers = creditService.findSuitableCreditOffers(customerId, amount, termMonths);

            if (offers.isEmpty()) {
                System.out.println("No suitable credit offers found.");
                return;
            }

            System.out.println("Suitable Credit Offers:");
            for (CreditOffer offer : offers) {
                System.out.println("Offer ID: " + offer.getOfferId() +
                        ", Interest Rate: " + offer.getInterestRate() + "%");
            }
        } catch (Exception e) {
            System.out.println("Failed to find suitable offers: " + e.getMessage());
        }
    }

    /**
     * Handles user input to process a credit application.
     *
     * @param scanner Scanner for user input.
     */
    private void processCreditApplication(Scanner scanner) {
        System.out.print("Enter Credit Offer ID: ");
        long creditOfferId = scanner.nextLong();

        System.out.print("Approve application? (true/false): ");
        boolean isApproved = scanner.nextBoolean();

        try {
            ActiveCredit processedOffer = creditService.processCreditApplication(creditOfferId, isApproved);
            System.out.println("Application Processed Successfully:");
            System.out.println("Status: " + processedOffer.getStatus());
        } catch (Exception e) {
            System.out.println("Failed to process application: " + e.getMessage());
        }
    }

    /**
     * Handles user input to view active credits for a customer.
     *
     * @param scanner Scanner for user input.
     */
    private void viewActiveCredits(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        long customerId = scanner.nextLong();

        try {
            List<CreditOffer> creditOffers = creditService.getActiveCreditsForCustomer(customerId);

            if (creditOffers.isEmpty()) {
                System.out.println("No active credits found for this customer.");
                return;
            }

            System.out.println("Active Credits:");
            for (CreditOffer creditOffer : creditOffers) {
                creditOffer.getActiveCredits().stream()
                        .forEach(activeCredit -> {
                            System.out.println("Active Credit ID: " + activeCredit.getActiveCreditId() +
                                    ", Amount: " + activeCredit.getLoanAmount() +
                                    ", Interest Rate: " + creditOffer.getInterestRate() + "%");
                        });
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve active credits: " + e.getMessage());
        }
    }

    /**
     * Handles user input to calculate early repayment for a credit offer.
     *
     * @param scanner Scanner for user input.
     */
    private void calculateEarlyRepayment(Scanner scanner) {
        System.out.print("Enter Credit Offer ID: ");
        long creditOfferId = scanner.nextLong();

        try {
            BigDecimal earlyRepaymentAmount = creditService.calculateEarlyRepayment(creditOfferId);
            System.out.println("Early Repayment Amount: " + earlyRepaymentAmount);
        } catch (Exception e) {
            System.out.println("Failed to calculate early repayment: " + e.getMessage());
        }
    }
}
