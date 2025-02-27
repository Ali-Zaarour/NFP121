package dev.alizaarour.services.pack;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.helper.DoneState;
import lombok.Getter;


@Getter
public class CountDoneVisitor implements CourseProcessVisitor {

    private int count = 0;

    @Override
    public void visit(CourseProcess cp) {
        if (cp.getState() instanceof DoneState) {
            count++;
        }
    }

}
