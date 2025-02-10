package dev.alizaarour.views.components;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.views.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class NavbarPanel extends JPanel {
    public NavbarPanel(BaseFrame current) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 50));
        setBackground(new Color(30, 144, 255));


        JLabel titleLabel = new JLabel("Online Training Center", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));


        JButton logoutButton = new JButton();
        logoutButton.setPreferredSize(new Dimension(35, 35));
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.addActionListener(e -> SessionManager.getInstance().logout(current));
        URL imageUrl = getClass().getResource("/images/logout.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            logoutButton.setIcon(new ImageIcon(image));
        }

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setContentAreaFilled(true);
                logoutButton.setBackground(new Color(255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setContentAreaFilled(false);
            }
        });


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);


        add(titleLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }
}
