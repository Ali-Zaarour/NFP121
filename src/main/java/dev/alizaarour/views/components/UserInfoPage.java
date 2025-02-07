package dev.alizaarour.views.components;

import dev.alizaarour.models.User;
import dev.alizaarour.services.UserService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import java.awt.*;

public class UserInfoPage implements Page, Observer {
    private final JPanel panel;
    private JTextField nameField, emailField, roleField, passwordField;

    public UserInfoPage() {
        panel = new JPanel();
        panel.setLayout(null); // Manual positioning

        UserService.getInstance().addObserver(this);

        createUI();
        updateUI();
    }

    private void createUI() {
        int panelWidth = 500;
        int labelWidth = 100;
        int fieldWidth = 250;
        int buttonWidth = 75;
        int fieldHeight = 30;
        int startX = 50;
        int startY = 30;
        int gapY = 50;

        JLabel titleLabel = new JLabel("User Profile", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(startX, startY, panelWidth - startX * 2, fieldHeight);
        panel.add(titleLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(startX, startY + 40, panelWidth - startX * 2, 5);
        panel.add(separator);

        int dataStartY = startY + 60; // Start after the title and separator

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(startX, dataStartY, labelWidth, fieldHeight);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(startX + labelWidth, dataStartY, fieldWidth, fieldHeight);
        nameField.setEditable(false);
        panel.add(nameField);

        JButton editNameButton = new JButton("edit");
        editNameButton.setBounds(startX + labelWidth + fieldWidth + 10, dataStartY, buttonWidth, 35);
        editNameButton.addActionListener(e -> new EditModal("Edit Name", "name"));
        panel.add(editNameButton);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(startX, dataStartY + gapY, labelWidth, fieldHeight);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(startX + labelWidth, dataStartY + gapY, fieldWidth, fieldHeight);
        emailField.setEditable(false);
        panel.add(emailField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(startX, dataStartY + 2 * gapY, labelWidth, fieldHeight);
        panel.add(roleLabel);

        roleField = new JTextField();
        roleField.setBounds(startX + labelWidth, dataStartY + 2 * gapY, fieldWidth, fieldHeight);
        roleField.setEditable(false);
        panel.add(roleField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(startX, dataStartY + 3 * gapY, labelWidth, fieldHeight);
        panel.add(passwordLabel);

        passwordField = new JTextField();
        passwordField.setBounds(startX + labelWidth, dataStartY + 3 * gapY, fieldWidth, fieldHeight);
        passwordField.setEditable(false);
        panel.add(passwordField);

        JButton editPasswordButton = new JButton("edit");
        editPasswordButton.setBounds(startX + labelWidth + fieldWidth + 10, dataStartY + 3 * gapY, buttonWidth, 35);
        editPasswordButton.addActionListener(e -> new EditModal("Edit Password", "password"));
        panel.add(editPasswordButton);
    }

    private void updateUI() {
        User user = UserService.getInstance().getActiveUser();

        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        roleField.setText(user.getRole().toString());

        passwordField.setText("*".repeat(user.getPsw().length()));
    }

    @Override
    public JPanel getPagePanel() {
        return panel;
    }

    @Override
    public void update() {
        updateUI();
    }
}
