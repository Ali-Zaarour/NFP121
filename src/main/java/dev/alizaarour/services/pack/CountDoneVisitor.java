package dev.alizaarour.services.pack;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.helper.DoneState;

// Counts the number of courses that have been completed (done).
public class CountDoneVisitor implements CourseProcessVisitor {
    private int count = 0;

    @Override
    public void visit(CourseProcess cp) {
        if (cp.getState() instanceof DoneState) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}
