package dev.alizaarour.views;

import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.helper.Role;
import dev.alizaarour.services.pack.UserFactory;
import dev.alizaarour.services.UserService;

import javax.swing.*;
import java.awt.*;

public class CreateAccount extends BaseFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<Role> roleComboBox;
    private JButton signUpButton;
    private JLabel goToLoginLabel;

    public CreateAccount() {
        super("create new account", 500, 500, true);
    }

    @Override
    protected void initVariable() {
        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        roleComboBox = new JComboBox<>(Role.values());
        signUpButton = new JButton("Sign Up");
        goToLoginLabel = new JLabel("<html><u>Already have an account? Log in</u></html>");
    }

    @Override
    protected void createComponents() {
        setLayout(null);

        JLabel titleLabel = new JLabel("Welcome", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(40, 30, 400, 30);
        add(titleLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(40, 70, 400, 20);
        add(nameLabel);

        nameField.setBounds(40, 95, 400, 35);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(40, 140, 400, 20);
        add(emailLabel);

        emailField.setBounds(40, 165, 400, 35);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 210, 400, 20);
        add(passwordLabel);

        passwordField.setBounds(40, 235, 400, 35);
        add(passwordField);

        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setBounds(40, 280, 400, 20);
        add(roleLabel);

        roleComboBox.setBounds(40, 305, 400, 35);
        add(roleComboBox);

        signUpButton.setBounds(40, 360, 400, 40);
        add(signUpButton);

        goToLoginLabel.setBounds(150, 410, 180, 25);
        goToLoginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(goToLoginLabel);


        signUpButton.addActionListener(e -> createUser());


        goToLoginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new Login();
            }
        });
    }

    private void createUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        Role selectedRole = (Role) roleComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRole == null) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserService.getInstance().findUserWithCondition(u -> u.getEmail().equals(email) || u.getName().equals(name)).isPresent()) {
            JOptionPane.showMessageDialog(this, "Email or Name already exist!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ApplicationInitializer.dataSchema.getUsers().add(UserFactory.createUser(name, email, password, selectedRole));
        JOptionPane.showMessageDialog(this, "User Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new Login();
    }
}
