package dev.alizaarour.services.pack;

import dev.alizaarour.views.TeacherHome;

public class TeacherRedirectStrategy implements LoginRedirectionStrategy {
    @Override
    public void redirect() {
        new TeacherHome();
    }
}
