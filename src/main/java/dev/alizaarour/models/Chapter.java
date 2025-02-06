package dev.alizaarour.models;


import lombok.*;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chapter implements TrainingMaterial {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private List<TrainingMaterial> materials;

    @Override
    public String display() {
        var list = new ArrayList<String>();
        for (TrainingMaterial material : materials) {
            list.add(material.display());
        }
        return "Chapter: " + title + "\n" + String.join("\n", list);
    }

}
