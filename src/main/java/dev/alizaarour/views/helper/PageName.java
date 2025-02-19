package dev.alizaarour.views.helper;

import lombok.Getter;

@Getter
public enum PageName {

    STUDENT_DASHBOARD("STUDENT_DASHBOARD"),
    STUDENT_COURSES("STUDENT_COURSES"),
    STUDENT_RESULTS("STUDENT_RESULTS"),

    TEACHER_MANAGE_COURSES("TEACHER_MANAGE_COURSES"),
    TEACHER_CREATE_COURSE("TEACHER_CREATE_COURSE"),
    TEACHER_MANAGE_MEETING("TEACHER_MANAGE_MEETING"),

    PROFILE("PROFILE");

    private final String value;

    PageName(String value) {
        this.value = value;
    }
}
