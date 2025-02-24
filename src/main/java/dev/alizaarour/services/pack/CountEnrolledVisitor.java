package dev.alizaarour.services.pack;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.helper.EnrolledState;
import dev.alizaarour.models.helper.InProgressState;

// Counts the number of courses that are still in an enrolled state.
// Here, we assume that a course process is considered "enrolled" if its state is either Enrolled or In - Progress.
public class CountEnrolledVisitor implements CourseProcessVisitor {
    private int count = 0;

    @Override
    public void visit(CourseProcess cp) {
        // Assuming a state is implemented with the State pattern, you can check its type.
        if (cp.getState() instanceof EnrolledState || cp.getState() instanceof InProgressState) {
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}
