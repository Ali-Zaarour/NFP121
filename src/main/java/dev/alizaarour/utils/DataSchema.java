package dev.alizaarour.utils;

import dev.alizaarour.models.Admin;
import dev.alizaarour.models.Student;
import dev.alizaarour.models.Teacher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class DataSchema implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Student> students;
    private List<Teacher> teachers;
    private Admin admin;
    
}
