package dev.alizaarour.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@SuperBuilder
public class Admin extends User{

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String displayRole() {
        return Privileges.ADMIN.getValue();
    }
}
