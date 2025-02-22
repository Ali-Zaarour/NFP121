package dev.alizaarour.models;


import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.helper.CourseProcessState;
import dev.alizaarour.models.helper.EnrolledState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CourseProcess {
    private int processId;
    private int courseId;
    private int levelId;
    private CourseProcessState state;
    private double paymentFees;
    private boolean paid;
    private LocalDate paymentDate;
    private boolean quizDone;
    private LocalDate quizDate;
    private double quizNote;

    public CourseProcess(int courseId, int levelId, double paymentFees) {
        this.courseId = courseId;
        this.levelId = levelId;
        this.paymentFees = paymentFees;
        this.state = new EnrolledState();
        this.processId = ApplicationInitializer.dataSchema.getCourseProcessSeq() + 1;
        ApplicationInitializer.dataSchema.setCourseProcessSeq(ApplicationInitializer.dataSchema.getCourseProcessSeq() + 1);
    }

    // Trigger a state transition
    public void nextState() {
        state.nextState(this);
    }
}
