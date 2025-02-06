package dev.alizaarour;

import dev.alizaarour.config.StandardInitializer;

public class Main {
    public static void main(String[] args) {
        try {
            new StandardInitializer().initializeApplication();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}