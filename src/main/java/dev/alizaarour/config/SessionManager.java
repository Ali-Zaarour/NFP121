package dev.alizaarour.config;

import dev.alizaarour.Main;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.User;
import dev.alizaarour.services.UserService;
import dev.alizaarour.services.pack.LoginRedirectContext;
import dev.alizaarour.utils.DataAction;
import dev.alizaarour.views.BaseFrame;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class SessionManager {

    private static SessionManager currentSessionManager;
    private User user;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (currentSessionManager == null) currentSessionManager = new SessionManager();
        return currentSessionManager;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void login(BaseFrame logInFrame, String email, String psw) {
        var currentUser = UserService.getInstance().findUserWithCondition(u -> u.getEmail().equals(email));
        if (currentUser.isPresent()) {
            if (currentUser.get().getPsw().equals(psw)) {
                this.user = currentUser.get();
                logInFrame.dispose();
                new LoginRedirectContext().redirectUser(this.user);
            } else
                JOptionPane.showMessageDialog(logInFrame, "Password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
        } else
            JOptionPane.showMessageDialog(logInFrame, "Email is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void logout() {
        System.out.println("Logging out...");

        try {
            DataAction.serialize(ApplicationInitializer.dataSchema, StandardApplicationProperties.getInstance().getDataPath() + "/data_schema.ser");
            System.out.println("Data saved.");
        } catch (Exception x) {
            System.err.println("Error saving: " + x.getMessage());
        }

        for (Frame frame : Frame.getFrames()) {
            if (frame.isVisible()) {
                frame.dispose();
            }
        }
        restartApplication();
    }


    private void restartApplication() {
        try {
            String javaBin = System.getProperty("java.home") + "/bin/java";
            String classPath = System.getProperty("java.class.path");
            String className = Main.class.getName();

            ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classPath, className);
            builder.start();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
