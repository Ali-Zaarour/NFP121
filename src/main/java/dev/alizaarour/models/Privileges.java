package dev.alizaarour.models;

import lombok.Getter;

@Getter
public enum Privileges {
    ADMIN("Admin"),
    STUDENT("Student"),
    TEACHER("Teacher");

    private final String value;

    Privileges(String value) {
        this.value = value;
    }

}

