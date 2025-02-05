package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Admin extends User{

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String displayRole() {
        return Privileges.ADMIN.getValue();
    }
}
