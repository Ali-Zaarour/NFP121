package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

import javax.swing.*;
import java.io.Serializable;

public class DoneState implements CourseProcessState, Serializable {
    @Override
    public void nextState(CourseProcess context) {
        JOptionPane.showMessageDialog(null, "Course is already completed.");
    }

    @Override
    public String getName() {
        return "Done";
    }
}
