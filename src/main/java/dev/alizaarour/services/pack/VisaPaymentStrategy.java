package dev.alizaarour.services.pack;

import javax.swing.*;

public class VisaPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
       //option pane just a simulation
        JOptionPane.showMessageDialog(null, "Payment of " + amount + " done using Visa");
        return true;
    }
}
