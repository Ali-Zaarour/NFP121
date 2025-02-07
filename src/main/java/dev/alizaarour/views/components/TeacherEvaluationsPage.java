package dev.alizaarour.views.components;

import javax.swing.*;
import java.awt.*;

public class TeacherEvaluationsPage implements Page {
    @Override
    public JPanel getPagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Student Evaluations", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
