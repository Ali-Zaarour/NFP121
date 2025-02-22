package dev.alizaarour.services;

import dev.alizaarour.services.pack.PaymentStrategy;
import dev.alizaarour.utils.Observable;

import javax.swing.*;
import java.time.LocalDate;

public class PaymentService extends Observable {

    private static final PaymentService instance = new PaymentService();

    private PaymentService() {
    }

    public static synchronized PaymentService getInstance() {
        return instance;
    }


    // Process payment using the provided PaymentStrategy.
    public void processPayment(int courseProcessId, double fees, PaymentStrategy paymentStrategy) {
        var courseProcess = CourseProcessService.getInstance().getCourseProcess(courseProcessId);
        boolean success = paymentStrategy.pay(fees);
        if (success) {
            courseProcess.setPaid(true);
            courseProcess.setPaymentDate(LocalDate.now());
            courseProcess.setPaymentFees(fees);
        } else JOptionPane.showMessageDialog(null, "Payment failed.");
    }
}
