package dev.alizaarour.views.components;

import javax.swing.*;
import java.awt.*;

public class TeacherManageCoursesPage implements Page {
    @Override
    public JPanel getPagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Manage Your Courses", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
