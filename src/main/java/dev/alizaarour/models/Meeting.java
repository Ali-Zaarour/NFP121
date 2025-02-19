package dev.alizaarour.models;

import dev.alizaarour.config.pack.ApplicationInitializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class Meeting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int meetingId;
    private LocalDate date;
    private LocalTime time;
    private int duration; // Duration in minutes
    private int teacherId;
    private int courseId;
    private int courseLevelId;
    private List<Integer> studentIds;
    private Boolean done;

    public Meeting(LocalDate date, LocalTime time, int duration, int teacherId,
                   int courseId, int courseLevelId, List<Integer> studentIds) {
        this.meetingId = ApplicationInitializer.dataSchema.getMeetingSeq() + 1;
        ApplicationInitializer.dataSchema.setMeetingSeq(ApplicationInitializer.dataSchema.getMeetingSeq() + 1);
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.courseLevelId = courseLevelId;
        this.studentIds = studentIds;
        this.done = false;
    }


    public void addStudent(int studentId) {
        if (!studentIds.contains(studentId)) {
            studentIds.add(studentId);
        }
    }

    public void removeStudent(int studentId) {
        studentIds.remove(Integer.valueOf(studentId));
    }
}

