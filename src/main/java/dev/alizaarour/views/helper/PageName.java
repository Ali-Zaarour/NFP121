package dev.alizaarour.views.helper;

import lombok.Getter;

@Getter
public enum PageName {

    STUDENT_DASHBOARD("STUDENT_DASHBOARD"),
    STUDENT_COURSES("STUDENT_COURSES"),
    STUDENT_RESULTS("STUDENT_RESULTS"),

    TEACHER_MANAGE_COURSES("TEACHER_MANAGE_COURSES"),
    TEACHER_EVALUATIONS("TEACHER_EVALUATIONS"),
    TEACHER_ATTENDANCE("TEACHER_ATTENDANCE"),
    TEACHER_CREATE_COURSE("TEACHER_CREATE_COURSE"),

    PROFILE("PROFILE");

    private final String value;

    PageName(String value) {
        this.value = value;
    }
}
