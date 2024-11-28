public interface TransactionFeeStrategy {
    double calculateFee(double amount);
}

class NoFeeStrategy implements TransactionFeeStrategy {
    @Override
    public double calculateFee(double amount) {
        return 0.0;
    }
}

class FixedFeeStrategy implements TransactionFeeStrategy {
    private double fee;

    public FixedFeeStrategy(double fee) {
        this.fee = fee;
    }

    @Override
    public double calculateFee(double amount) {
        return fee;
    }
}

class PercentageFeeStrategy implements TransactionFeeStrategy {
    private double percentage;

    public PercentageFeeStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double calculateFee(double amount) {
        return amount * (percentage / 100);
    }
}