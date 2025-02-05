package dev.alizaarour.models;

public class PayPalPayment implements PaymentStrategy {

    @Override
    public String pay(double amount) {
        return "Paid $" + amount + " via PayPal.";
    }
}
