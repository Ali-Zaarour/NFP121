package dev.alizaarour.models;


import lombok.*;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineBankingPayment implements PaymentStrategy {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String pay(double amount) {
        return "Paid $" + amount + " via Online Banking.";
    }
}
