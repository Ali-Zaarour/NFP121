package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

import java.io.Serializable;

public class EnrolledState implements CourseProcessState, Serializable {
    @Override
    public void nextState(CourseProcess context) {
        if (context.isPaid()) {
            context.setState(new InProgressState());
        } else {
            System.out.println("Payment not completed. Cannot transition from EnrolledState.");
        }
    }
}