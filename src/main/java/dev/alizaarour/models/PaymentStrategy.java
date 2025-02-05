package dev.alizaarour.models;

import java.io.Serializable;

public interface PaymentStrategy{
    String pay(double amount);
}
