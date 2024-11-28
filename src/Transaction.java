public class Transaction {
    private String transactionId;
    private String accountId;
    private String description;
    private double amount;
    private String date;

    public Transaction(String accountId, String description, double amount) {
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.accountId = accountId;
        this.description = description;
        this.amount = amount;
        this.date = java.time.LocalDateTime.now().toString();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}
