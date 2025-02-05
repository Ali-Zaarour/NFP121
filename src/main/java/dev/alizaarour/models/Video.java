package dev.alizaarour.models;

import lombok.*;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video implements TrainingMaterial {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    @Override
    public String display() {
        return "Video: " + title;
    }
}
