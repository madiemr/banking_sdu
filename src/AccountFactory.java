public class AccountFactory {
    public static BankAccount createAccount(String type, double... args) {
        switch (type.toLowerCase()) {
            case "savings":
                return new SavingsAccount();
            case "current":
                return new CurrentAccount();
            case "credit":
                if (args.length < 1) throw new IllegalArgumentException("Credit limit required.");
                return new CreditAccount(args[0]);
            case "deposit":
                if (args.length < 1) throw new IllegalArgumentException("Interest rate required.");
                return new DepositAccount(args[0]);
            default:
                throw new IllegalArgumentException("Invalid account type.");
        }
    }
}