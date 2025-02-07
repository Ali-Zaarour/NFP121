package dev.alizaarour.views.components;

import javax.swing.*;
import java.awt.*;

public class NavbarPanel extends JPanel {
    public NavbarPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 50));
        setBackground(new Color(30, 144, 255));

        JLabel titleLabel = new JLabel("Online Training Center", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        add(titleLabel, BorderLayout.CENTER);
    }
}
