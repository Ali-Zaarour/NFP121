package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class Payment {

    private PaymentStrategy paymentStrategy;

    public void processPayment(double amount) {
        paymentStrategy.pay(amount);
    }
}
