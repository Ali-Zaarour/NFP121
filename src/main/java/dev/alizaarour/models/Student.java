package dev.alizaarour.models;

import dev.alizaarour.config.pack.ApplicationInitializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Student extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    public Student(String name, String email, String psw, Role role) {
        super(ApplicationInitializer.dataSchema.getUserSeq() + 1, name, email, psw, Role.STUDENT);
        ApplicationInitializer.dataSchema.setUserSeq(ApplicationInitializer.dataSchema.getUserSeq() + 1);
    }
}
