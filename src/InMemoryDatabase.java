import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {
    private List<User> users;
    private List<Transaction> transactions = new ArrayList<>();

    private List<CardRequest> cardRequests = new ArrayList<>();

    public InMemoryDatabase() {
        users = new ArrayList<>();
        // Add an admin by default
        users.add(new Administrator("Admin", "admin", "admin123"));
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }
    public void addCardRequest(CardRequest request) {
        cardRequests.add(request);
    }

    public List<CardRequest> getCardRequests() {
        return cardRequests;
    }
    public List<Transaction> getTransactionsByAccount(String accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountId().equals(accountId)) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public BankAccount findAccount(String accountId) {
        for (User user : users) { // users - это список всех пользователей
            if (user instanceof Client) {
                Client client = (Client) user;
                for (BankAccount account : client.getAccounts()) {
                    if (account.getAccountId().equals(accountId)) {
                        return account;
                    }
                }
            }
        }
        return null;
    }
    public List<User> getAllUsers() {
        return users; // Предполагается, что `users` — это список всех пользователей
    }



}