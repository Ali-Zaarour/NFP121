package dev.alizaarour.services;

import dev.alizaarour.models.Role;
import dev.alizaarour.models.User;
import dev.alizaarour.services.pack.LoginRedirectionStrategy;
import dev.alizaarour.services.pack.StudentRedirectStrategy;
import dev.alizaarour.services.pack.TeacherRedirectStrategy;

import java.util.HashMap;
import java.util.Map;

public class LoginRedirectContext {
    private final Map<Role, LoginRedirectionStrategy> strategies = new HashMap<>();

    public LoginRedirectContext() {
        strategies.put(Role.STUDENT, new StudentRedirectStrategy());
        strategies.put(Role.TEACHER, new TeacherRedirectStrategy());
    }

    public void redirectUser(User user) {
        strategies.get(user.getRole()).redirect();
    }
}
