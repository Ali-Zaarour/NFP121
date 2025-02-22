package dev.alizaarour.models.helper;

import dev.alizaarour.models.*;

public class CourseContentFactory {
    public static CourseContent createContent(ContentType type, String content) {
        return switch (type) {
            case DOCUMENT -> new Document(content);
            case VIDEO -> new Video(content);
            case IMAGE -> new Image(content);
            case VOICE -> new Voice(content);
        };
    }
}
