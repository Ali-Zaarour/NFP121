package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

import java.io.Serializable;

public class DoneState implements CourseProcessState, Serializable {

    @Override
    public void nextState(CourseProcess context) {
    }

    @Override
    public String getName() {
        return "Done";
    }
}
