package dev.alizaarour.models;

import java.io.Serializable;

public interface PaymentStrategy extends Serializable {
    String pay(double amount);
}
