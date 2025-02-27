package dev.alizaarour.services.pack;

public interface PaymentStrategy {

    boolean pay(double amount);
}
