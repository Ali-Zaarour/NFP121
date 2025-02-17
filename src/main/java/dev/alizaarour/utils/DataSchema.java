package dev.alizaarour.utils;

import dev.alizaarour.models.Course;
import dev.alizaarour.models.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DataSchema extends Observable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int userSeq;
    private int courseSeq;
    private List<User> users;
    private List<Course> courses;

    public DataSchema() {
        this.users = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public void addC(Course course) {
        courses.add(course);
        notifyObservers();
    }

    public void removeC(Course course) {
        courses.remove(course);
        notifyObservers();
    }

    public void addU(User user) {
        users.add(user);
        notifyObservers();
    }

    public void removeU(User user) {
        users.remove(user);
        notifyObservers();
    }

    public <T extends User> List<T> getUsersByType(Class<T> type) {
        return users.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }
}
