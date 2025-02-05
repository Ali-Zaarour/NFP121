package dev.alizaarour.models;

public class OnlineBankingPayment implements PaymentStrategy {

    @Override
    public String pay(double amount) {
        return "Paid $" + amount + " via Online Banking.";
    }
}
