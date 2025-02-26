package dev.alizaarour.services;

import dev.alizaarour.services.pack.PaymentStrategy;
import dev.alizaarour.services.pack.VisaPaymentStrategy;
import dev.alizaarour.utils.Observable;

import javax.swing.*;
import java.time.LocalDate;

public class PaymentService extends Observable {

    private static PaymentService instance = new PaymentService();

    private PaymentService() {
    }

    public static synchronized PaymentService getInstance() {
        if (instance == null) instance = new PaymentService();
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
            CourseService.getInstance().addNewEnrolledUser(courseProcess.getCourseId());
            courseProcess.setPaymentType(paymentStrategy instanceof VisaPaymentStrategy ? "Visa" : "OMT");
        } else JOptionPane.showMessageDialog(null, "Payment failed.");
        notifyObservers();
    }

    //clean
    public void clean() {
        if (instance != null)
            instance = null;
    }
}
