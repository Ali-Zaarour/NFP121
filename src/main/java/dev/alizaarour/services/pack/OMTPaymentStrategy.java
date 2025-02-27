package dev.alizaarour.services.pack;

import javax.swing.*;

public class OMTPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        JOptionPane.showMessageDialog(null, "Payment of " + amount + " done using OMT");
        return true;
    }
}
