package dev.alizaarour.views.components;

import dev.alizaarour.views.pack.Command;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SidebarPanel extends JPanel {
    private static final int BUTTON_WIDTH = 190;
    private static final int BUTTON_HEIGHT = 35;
    private static final int BUTTON_SPACING = 5;
    private static final int START_Y = 10;

    public SidebarPanel(List<JButton> buttons) {
        setLayout(null);
        setPreferredSize(new Dimension(200, 750));
        setBackground(new Color(230, 230, 230));

        int yPosition = START_Y; // Y position for buttons

        for (JButton button : buttons) {
            button.setBounds(5, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            add(button);
            yPosition += BUTTON_HEIGHT + BUTTON_SPACING;
        }
    }

    // Helper method to create buttons with a Command
    public static JButton createSidebarButton(String text, Command command) {
        JButton button = new JButton(text);
        button.addActionListener(e -> command.execute());
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return button;
    }
}
