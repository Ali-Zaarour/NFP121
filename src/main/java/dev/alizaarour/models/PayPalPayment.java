package dev.alizaarour.models;

import java.io.Serial;

public class PayPalPayment implements PaymentStrategy {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String pay(double amount) {
        return "Paid $" + amount + " via PayPal.";
    }
}
