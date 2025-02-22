package dev.alizaarour.views.pack;

import dev.alizaarour.views.components.*;
import dev.alizaarour.views.helper.PageName;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class PageFactory {
    private static final Map<PageName, Page> pageCache = new HashMap<>();

    public static Page getPage(PageName pageName) {
        return pageCache.computeIfAbsent(pageName, key -> {
            return switch (key) {
                case STUDENT_DASHBOARD -> new StudentDashboardPage();
                case STUDENT_COURSES -> new StudentCoursesPage();
                case STUDENT_MEETINGS -> new StudentManageMeeting();
                case STUDENT_PAYMENT -> new StudentPaymentHistory();
                case STUDENT_RESULTS -> new StudentResultsPage();
                case TEACHER_MANAGE_COURSES -> new TeacherManageCoursesPage();
                case TEACHER_CREATE_COURSE -> new TeacherCreateCoursePage();
                case TEACHER_MANAGE_MEETING -> new TeacherManageMeeting();
                case PROFILE -> new UserInfoPage();
                default -> () -> {
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Page Not Found!", SwingConstants.CENTER));
                    return panel;
                };
            };
        });
    }
}