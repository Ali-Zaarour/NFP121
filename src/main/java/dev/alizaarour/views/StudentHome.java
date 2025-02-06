package dev.alizaarour.views;

import javax.swing.*;

public class StudentHome extends BaseFrame {

    public StudentHome() {
        super("student home page", 1200, 800, true);
    }

    @Override
    protected void createComponents() {

    }

    @Override
    protected void initVariable() {
        JLabel label = new JLabel("Welcome, Student!", SwingConstants.CENTER);
        label.setBounds(150, 200, 200, 50);
        add(label);
    }
}
