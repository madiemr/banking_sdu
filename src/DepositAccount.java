public class DepositAccount extends BankAccount {
    private double interestRate;

    public DepositAccount(double interestRate) {
        super();
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "Deposit Account";
    }

    public void applyInterest() {
        double interest = getBalance() * (interestRate / 100);
        deposit(interest);
        System.out.println("Applied interest: " + interest + ". New balance: " + getBalance());
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}