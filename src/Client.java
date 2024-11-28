import exceptions.BankingException;
import exceptions.InsufficientFundsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client extends User {
    private List<BankAccount> accounts;
    private static final Logger logger = LoggerConfig.getLogger(Client.class.getName());


    public Client(String name, String username, String password) {
        super(name, username, password);
        this.accounts = new ArrayList<>();
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }


    @Override
    public void showMenu(Scanner scanner, InMemoryDatabase database) {
        boolean running = true;

        while (running) {
            printMenu(); // Печатаем меню

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            handleMenuOption(choice, scanner, database); // Обрабатываем выбор
        }
    }

    private void printMenu() {
        System.out.println("\nAdministrator Menu:");
        System.out.println("1. View All Users");
        System.out.println("2. Block/Unblock Cards");
        System.out.println("3. View All Transactions");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");
    }

    private void handleMenuOption(int choice, Scanner scanner, InMemoryDatabase database) {
        try {
            switch (choice) {
                case 1 -> viewAccounts();
                case 2 -> openAccount(scanner);
                case 3 -> openSpecialAccount(scanner);
                case 4 -> depositMoney(scanner, database);
                case 5 -> withdrawMoney(scanner, database);
                case 6 -> transferMoney(scanner, database);
                case 7 -> sendCardRequest(scanner, database);
                case 8 -> viewTransactionHistory(scanner, database);
                case 9 -> {
                    System.out.println("Logging out...");
                    return; // Выход из меню
                }
                default -> throw new IllegalArgumentException("Invalid menu option. Please choose a valid number.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            logger.warning("Error during client operation: " + e.getMessage());
        }
    }




    private void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts.");
            return;
        }
        for (BankAccount account : accounts) {
            System.out.println(account.getAccountType() + " | ID: " + account.getAccountId() + " | Balance: " + account.getBalance());
        }
    }

    private void openAccount(Scanner scanner) {
        System.out.print("Enter account type (savings/current): ");
        String type = scanner.nextLine();
        try {
            BankAccount account = AccountFactory.createAccount(type);
            addAccount(account);
            System.out.println(type + " account created successfully. Account ID: " + account.getAccountId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void transferMoney(Scanner scanner, InMemoryDatabase database) {
        System.out.print("Enter your account ID: ");
        String fromAccountId = scanner.nextLine();
        BankAccount fromAccount = findAccount(fromAccountId);

        if (fromAccount == null) {
            System.out.println("Invalid source account ID.");
            logger.warning("Transfer failed: Invalid source account ID " + fromAccountId);
            return;
        }

        System.out.print("Enter recipient account ID: ");
        String toAccountId = scanner.nextLine();
        BankAccount toAccount = database.findAccount(toAccountId);

        if (toAccount == null) {
            System.out.println("Recipient account not found.");
            logger.warning("Transfer failed: Recipient account ID " + toAccountId + " not found.");
            return;
        }

        System.out.print("Enter transfer amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            logger.warning("Transfer failed: Negative or zero amount.");
            return;
        }

        try {
            if (fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                System.out.println("Transferred " + amount + " from account " + fromAccountId + " to " + toAccountId);

                // Add transactions
                database.addTransaction(new Transaction(fromAccountId, "Transfer to " + toAccountId, -amount));
                database.addTransaction(new Transaction(toAccountId, "Transfer from " + fromAccountId, amount));

                logger.info("Transfer of " + amount + " from " + fromAccountId + " to " + toAccountId + " successful.");
            } else {
                throw new InsufficientFundsException("Insufficient funds in account " + fromAccountId);
            }
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
            logger.warning(e.getMessage());
        }
    }



    private void depositMoney(Scanner scanner, InMemoryDatabase database) {
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts to deposit money into.");
            return;
        }

        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        BankAccount account = findAccount(accountId);
        if (account == null) {
            System.out.println("Invalid account ID.");
            return;
        }

        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }

        account.deposit(amount);
        System.out.println("Deposited " + amount + " into account " + accountId);

        // Add transaction
        database.addTransaction(new Transaction(accountId, "Deposit", amount));
    }


    private void withdrawMoney(Scanner scanner, InMemoryDatabase database) {
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts to withdraw money from.");
            return;
        }

        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        BankAccount account = findAccount(accountId);

        if (account == null) {
            System.out.println("Invalid account ID.");
            logger.warning("Withdrawal failed: Invalid account ID " + accountId);
            return;
        }

        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            logger.warning("Withdrawal failed: Negative or zero amount.");
            return;
        }

        try {
            if (account.withdraw(amount)) {
                System.out.println("Withdrew " + amount + " from account " + accountId);
                database.addTransaction(new Transaction(accountId, "Withdrawal", -amount));
                logger.info("Withdrawal of " + amount + " from account " + accountId + " successful.");
            } else {
                throw new InsufficientFundsException("Insufficient funds in account " + accountId);
            }
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
            logger.warning(e.getMessage());
        }
    }



    private BankAccount findAccount(String accountId) {
        for (BankAccount account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }
    private void openSpecialAccount(Scanner scanner) {
        System.out.print("Enter account type (credit/deposit): ");
        String type = scanner.nextLine();
        try {
            if (type.equalsIgnoreCase("credit")) {
                System.out.print("Enter credit limit: ");
                double limit = scanner.nextDouble();
                scanner.nextLine();
                BankAccount account = AccountFactory.createAccount(type, limit);
                addAccount(account);
                System.out.println("Credit account created successfully. ID: " + account.getAccountId());
            } else if (type.equalsIgnoreCase("deposit")) {
                System.out.print("Enter interest rate: ");
                double rate = scanner.nextDouble();
                scanner.nextLine();
                BankAccount account = AccountFactory.createAccount(type, rate);
                addAccount(account);
                System.out.println("Deposit account created successfully. ID: " + account.getAccountId());
            } else {
                System.out.println("Invalid account type.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void sendCardRequest(Scanner scanner, InMemoryDatabase database) {
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts to send requests for.");
            return;
        }

        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        BankAccount account = findAccount(accountId);
        if (account == null) {
            System.out.println("Invalid account ID.");
            return;
        }

        System.out.print("Enter request type (block/unblock): ");
        String type = scanner.nextLine().toLowerCase();

        CardRequest.RequestType requestType;
        if (type.equals("block")) {
            requestType = CardRequest.RequestType.BLOCK;
        } else if (type.equals("unblock")) {
            requestType = CardRequest.RequestType.UNBLOCK;
        } else {
            System.out.println("Invalid request type.");
            return;
        }

        CardRequest request = new CardRequest(accountId, requestType);
        database.addCardRequest(request);
        System.out.println("Request sent successfully: " + request);
    }

    private void viewTransactionHistory(Scanner scanner, InMemoryDatabase database) {
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts.");
            return;
        }

        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        BankAccount account = findAccount(accountId);
        if (account == null) {
            System.out.println("Invalid account ID.");
            return;
        }

        List<Transaction> transactions = database.getTransactionsByAccount(accountId);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for account " + accountId);
            return;
        }

        System.out.println("Transaction History for account " + accountId + ":");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private void viewAllTransactions(InMemoryDatabase database) {
        List<Transaction> transactions = database.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("All Transactions:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }


}