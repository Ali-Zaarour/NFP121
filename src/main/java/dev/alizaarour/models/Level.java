package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Level implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int num;
    private String title;
    private double fees;
    private List<Chapter> chapters;
    private Quiz quiz;


    public Level(int num, int fees) {
        this.num = num;
        this.fees = fees;
        this.chapters = new ArrayList<>();
    }

    public String toString() {
        return num + " - " + title + " - chapters: " + chapters.size() + " - fees: " + fees;
    }
}
