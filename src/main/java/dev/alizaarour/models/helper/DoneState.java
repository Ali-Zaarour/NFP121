package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

import javax.swing.*;

public class DoneState implements CourseProcessState {
    @Override
    public void nextState(CourseProcess context) {
        JOptionPane.showMessageDialog(null, "Course is already completed.");
    }
}
