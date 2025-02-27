package dev.alizaarour.services;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.Course;
import dev.alizaarour.utils.Observable;

import java.util.List;

public class CourseService extends Observable {

    private static CourseService instance = new CourseService();

    private CourseService() {
    }

    public static synchronized CourseService getInstance() {
        if (instance == null) instance = new CourseService();
        return instance;
    }

    public void addCourse(Course newCourse) {
        ApplicationInitializer.dataSchema.getCourses().add(newCourse);
        notifyObservers();
    }

    //get course by id
    public Course getCourseById(int courseID) {
        Course course;
        for (int i = 0; i < ApplicationInitializer.dataSchema.getCourses().size(); i++) {
            course = ApplicationInitializer.dataSchema.getCourses().get(i);
            if (course.getPk() == courseID) {
                return course;
            }
        }
        return null;
    }

    public void removeCourse(int courseID) {
        ApplicationInitializer.dataSchema.getCourses().removeIf(course -> course.getPk() == courseID);
        notifyObservers();
    }

    public List<Course> getCoursesForCurrentTeacher() {
        // Fetch courses for the logged-in teacher
        String teacherEmail = SessionManager.getInstance().getUser().getEmail();
        return ApplicationInitializer.dataSchema.getCourses().stream()
                .filter(course -> course.getCreatedBy().equals(teacherEmail))
                .toList();
    }

    // replace a course object with a new one by id
    public void updateCourse(int courseID, Course newCourse) {
        ApplicationInitializer.dataSchema.getCourses().removeIf(course -> course.getPk() == courseID);
        ApplicationInitializer.dataSchema.getCourses().add(newCourse);
        notifyObservers();
    }

    public int getTotalCourses() {
        int ttl = 0;
        var c = ApplicationInitializer.dataSchema.getCourses();
        for (Course course : c)
            for (int j = 0; j < course.getLevels().size(); j++)
                ttl++;
        return ttl;
    }

    public Object[][] getCourses() {
        //String[] columnNames = {"Num", "Title", "Created By", "Levels", "Enrolled", "Action"};
        Object[][] courses = new Object[ApplicationInitializer.dataSchema.getCourses().size()][6];
        for (int i = 0; i < ApplicationInitializer.dataSchema.getCourses().size(); i++) {
            courses[i][0] = ApplicationInitializer.dataSchema.getCourses().get(i).getPk();
            courses[i][1] = ApplicationInitializer.dataSchema.getCourses().get(i).getTitle();
            int finalI = i;
            courses[i][2] = UserService.getInstance()
                    .findUserWithCondition(u -> u.getEmail()
                            .equals(ApplicationInitializer
                                    .dataSchema
                                    .getCourses()
                                    .get(finalI)
                                    .getCreatedBy())
                    ).get().getName();
            courses[i][3] = ApplicationInitializer.dataSchema.getCourses().get(i).getLevels().size();
            courses[i][4] = ApplicationInitializer.dataSchema.getCourses().get(i).getStudentsEnrolled().size();
            courses[i][5] = "Enroll";
        }
        return courses;
    }

    public void addNewEnrolledUser(int courseId) {
        var course = getCourseById(courseId);
        var currentUserId = UserService.getInstance().getActiveUser().getUserId();
        for (Integer id : course.getStudentsEnrolled()) {
            if (currentUserId == id)
                return;
        }
        course.getStudentsEnrolled().add(currentUserId);
        notifyObservers();
    }

    //clean
    public void clean() {
        if (instance != null)
            instance = null;
    }
}
