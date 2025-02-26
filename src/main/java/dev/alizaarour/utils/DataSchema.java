package dev.alizaarour.utils;

import dev.alizaarour.models.Course;
import dev.alizaarour.models.Meeting;
import dev.alizaarour.models.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DataSchema extends Observable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int userSeq;
    private int courseSeq;
    private int meetingSeq;
    private int courseProcessSeq;
    private List<User> users = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Meeting> meetings = new ArrayList<>();
}
