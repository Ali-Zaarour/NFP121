package dev.alizaarour.services.pack;

import dev.alizaarour.models.CourseProcess;

public interface CourseProcessVisitor {

    void visit(CourseProcess cp);
}
