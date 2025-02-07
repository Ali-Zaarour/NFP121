package dev.alizaarour.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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

    public Course(String javaBasics, String mail, String s) {
        this.title = javaBasics;
        this.createdBy = mail;
        this.objective = s;
    }

    public void display() {
        System.out.println("Course details");
        for (Level lv : levels) {
            lv.display();
        }
    }
}
