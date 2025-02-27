package dev.alizaarour.models.helper;

import dev.alizaarour.models.CourseProcess;

public interface CourseProcessState {

    void nextState(CourseProcess context);

    String getName();
}
