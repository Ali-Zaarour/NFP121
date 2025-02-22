package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

public class EnrolledState implements CourseProcessState {
    @Override
    public void nextState(CourseProcess context) {
        if (context.isPaid()) {
            context.setState(new InProgressState());
        } else {
            System.out.println("Payment not completed. Cannot transition from EnrolledState.");
        }
    }
}