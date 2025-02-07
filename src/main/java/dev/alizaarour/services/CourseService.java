package dev.alizaarour.services;

import dev.alizaarour.utils.Observable;

public class CourseService extends Observable {
    
    private static final CourseService instance = new CourseService();

    private CourseService() {
    }

    public static synchronized CourseService getInstance() {
        return instance;
    }
}
