package dev.alizaarour.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Teacher extends User {

    @Serial
    private static final long serialVersionUID = 1L;


    public Teacher(String name, String email, String psw, Role role) {
        super(++num, name, email, psw, Role.TEACHER);
    }
}
