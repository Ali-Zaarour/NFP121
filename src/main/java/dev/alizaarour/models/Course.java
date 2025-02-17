package dev.alizaarour.models;

import dev.alizaarour.config.pack.ApplicationInitializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Course implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int pk;
    private String title;
    private String createdBy; //teacher email ref
    private String objective;
    private List<String> syllabus;
    private Set<Integer> studentsEnrolled;
    private Set<Integer> teachingBy;
    private List<Level> levels;

    public Course(String title, String createdBy, String objective) {
        this.title = title;
        this.createdBy = createdBy;
        this.objective = objective;
        syllabus = new ArrayList<>();
        studentsEnrolled = new TreeSet<>();
        teachingBy = new TreeSet<>();
        levels = new ArrayList<>();
        this.pk = ApplicationInitializer.dataSchema.getCourseSeq() + 1;
        ApplicationInitializer.dataSchema.setCourseSeq(ApplicationInitializer.dataSchema.getCourseSeq() + 1);
    }
}
