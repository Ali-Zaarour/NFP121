package dev.alizaarour;

import dev.alizaarour.config.StandardInitializer;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.Course;
import dev.alizaarour.models.User;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            new StandardInitializer().initializeApplication();
            for (User user : ApplicationInitializer.dataSchema.getUsers()) {
                System.out.println(user);
            }
            if (ApplicationInitializer.dataSchema.getMeetings()== null){
                ApplicationInitializer.dataSchema.setMeetings(new ArrayList<>());
            }

            if (ApplicationInitializer.dataSchema.getCourses()== null){
                ApplicationInitializer.dataSchema.setCourses(new ArrayList<>());
            }
            for (Course course : ApplicationInitializer.dataSchema.getCourses()) {
                System.out.println(course.getPk());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}