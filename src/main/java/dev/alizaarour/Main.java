package dev.alizaarour;

import dev.alizaarour.config.StandardInitializer;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.User;

public class Main {
    public static void main(String[] args) {
        try {
            new StandardInitializer().initializeApplication();
            for (User user : ApplicationInitializer.dataSchema.getUsers()) {
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}