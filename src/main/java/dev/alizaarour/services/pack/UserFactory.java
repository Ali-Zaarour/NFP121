package dev.alizaarour.services.pack;

import dev.alizaarour.models.Student;
import dev.alizaarour.models.Teacher;
import dev.alizaarour.models.User;
import dev.alizaarour.models.helper.Role;

public class UserFactory {

    public static User createUser(String name, String email, String psw, Role role) {
        if (role == Role.STUDENT) {
            return new Student(name, email, psw, Role.STUDENT);
        } else if (role == Role.TEACHER) {
            return new Teacher(name, email, psw, Role.TEACHER);
        } else {
            throw new IllegalArgumentException("Invalid Role: " + role);
        }
    }
}
