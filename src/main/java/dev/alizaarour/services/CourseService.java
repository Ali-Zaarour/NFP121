package dev.alizaarour.services;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.Course;
import dev.alizaarour.utils.Observable;

import java.util.List;

public class CourseService extends Observable {

    private static final CourseService instance = new CourseService();

    private CourseService() {
    }

    public static synchronized CourseService getInstance() {
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
        //iterate over courses and remove the course with the given id
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
}
