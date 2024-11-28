import java.util.UUID;

public abstract class BankAccount {
    private String accountId;
    private double balance;

    private boolean isActive;
    private TransactionFeeStrategy feeStrategy = new NoFeeStrategy();


    public BankAccount() {
        this.accountId = UUID.randomUUID().toString();
        this.balance = 0.0;
        this.isActive = true;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        balance += amount;
        System.out.println("Successfully deposited " + amount + ". New balance: " + balance);
    }

    public boolean withdraw(double amount) {
        double fee = feeStrategy.calculateFee(amount);
        double totalAmount = amount + fee;

        if (totalAmount > balance) {
            System.out.println("Insufficient funds including fee (" + fee + ").");
            return false;
        }
        balance -= totalAmount;
        System.out.println("Successfully withdrew " + amount + " (Fee: " + fee + "). New balance: " + balance);
        return true;
    }



    public void deactivate() {
        isActive = false;
    }
    public void setFeeStrategy(TransactionFeeStrategy strategy) {
        this.feeStrategy = strategy;
    }

    public abstract String getAccountType();
}