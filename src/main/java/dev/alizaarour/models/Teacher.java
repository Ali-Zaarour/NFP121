package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Teacher extends User{

    private static final long serialVersionUID = 1L;

    private List<Course> courses = new ArrayList<>();

    @Override
    public String displayRole() {
        return Privileges.STUDENT.getValue();
    }

    public void createCourse(Course course) {
        courses.add(course);
    }
}
