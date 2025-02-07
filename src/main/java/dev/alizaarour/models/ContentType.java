package dev.alizaarour.models;

import lombok.Getter;

@Getter
public enum ContentType {

    DOCUMENT("Document"),
    VIDEO("Video"),
    IMAGE("Image"),
    VOICE("Voice");

    private final String label;

    ContentType(String label) {
        this.label = label;
    }

}

