package dev.alizaarour.services.pack;

import dev.alizaarour.models.CourseProcess;
import lombok.Getter;

@Getter
public class TotalFeesVisitor implements CourseProcessVisitor {

    private double totalFees = 0.0;

    @Override
    public void visit(CourseProcess cp) {
        if (cp.isPaid()) {
            totalFees += cp.getPaymentFees();
        }
    }

}
