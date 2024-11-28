public class CreditAccount extends BankAccount {
    private double creditLimit;

    public CreditAccount(double creditLimit) {
        super();
        this.creditLimit = creditLimit;
    }

    @Override
    public String getAccountType() {
        return "Credit Account";
    }

    @Override
    public boolean withdraw(double amount) {
        double totalAvailable = getBalance() + creditLimit;
        if (amount > totalAvailable) {
            System.out.println("Withdrawal exceeds credit limit.");
            return false;
        }
        if (amount > getBalance()) {
            creditLimit -= (amount - getBalance());
            deposit(-getBalance()); // Set balance to 0
        } else {
            deposit(-amount);
        }
        System.out.println("Successfully withdrew " + amount + ". Remaining credit limit: " + creditLimit);
        return true;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }
}