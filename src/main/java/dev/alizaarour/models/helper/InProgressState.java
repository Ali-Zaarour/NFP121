package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

public class InProgressState implements CourseProcessState {

    @Override
    public void nextState(CourseProcess context) {
        if(context.isQuizDone()){
            context.setState(new DoneState());
            System.out.println("Transitioned to DoneState");
        } else {
            System.out.println("Quiz not completed. Remain in InProgressState.");
        }
    }
}
