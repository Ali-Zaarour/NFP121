package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Course> enrolledCourses;

    @Override
    public String displayRole() {
        return Privileges.STUDENT.getValue();
    }

    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
    }
}
