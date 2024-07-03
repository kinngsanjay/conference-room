package app.conferenceroom.test;

public class TransactionProcessor {


    public double calculateFee(String transactionType, double amount) {

        double fee = 0.0;
        if ("DEPOSIT".equals(transactionType)) {
            if (amount > 5000) {
                fee = 10.0; // Flat fee for high-value deposits
            } else {
                fee = 2.0; // Standard fee for deposits
            }
        } else if ("WITHDRAWAL".equals(transactionType)) {
            if (amount > 1000) {
                fee = amount * 0.02; // 2% fee for large withdrawals
            } else {
                fee = 5.0; // Flat fee for smaller withdrawals
            }
        }
        else if ("TRANSFER".equals(transactionType)) {
            if (amount > 2000) {
                fee = 20.0; // Higher fee for large transfers
            } else {
                fee = 10.0; // Standard fee for transfers
            }
        } else {
            fee = 1.0; // Default fee for unspecified transactions
        }

        return fee;
    }
}
