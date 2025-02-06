package dev.alizaarour.models;

import lombok.Getter;

@Getter
public enum Role {
    STUDENT("Student"),
    TEACHER("Teacher");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}

