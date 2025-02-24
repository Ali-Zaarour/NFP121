package dev.alizaarour.services.pack;

import dev.alizaarour.models.CourseProcess;

// Sums the payment fees of course processes that have been paid.
public class TotalFeesVisitor implements CourseProcessVisitor {

    private double totalFees = 0.0;

    @Override
    public void visit(CourseProcess cp) {
        if (cp.isPaid()) {
            totalFees += cp.getPaymentFees();
        }
    }

    public double getTotalFees() {
        return totalFees;
    }
}
