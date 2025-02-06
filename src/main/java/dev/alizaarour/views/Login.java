package dev.alizaarour.views;

import dev.alizaarour.config.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Login extends BaseFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel createAccountLabel;

    public Login() {
        super("Login Page", 500, 500, true);
    }

    @Override
    protected void initVariable() {
        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        createAccountLabel = new JLabel("<html><u>Create an account</u></html>"); // Underlined text
    }

    @Override
    protected void createComponents() {
        setLayout(null);


        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(215, 30, 150, 100); // Centered
        setImage(imageLabel);
        add(imageLabel);


        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(40, 150, 400, 20);
        add(emailLabel);

        emailField.setBounds(40, 175, 400, 35); // Increased height for modern UI
        add(emailField);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 225, 400, 20);
        add(passwordLabel);

        passwordField.setBounds(40, 250, 400, 35);
        add(passwordField);


        loginButton.setBounds(40, 310, 400, 40);
        add(loginButton);


        createAccountLabel.setBounds(190, 370, 150, 30);
        createAccountLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(createAccountLabel);


        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            SessionManager.getInstance().login(this, email, password);
        });


        createAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new CreateAccount();
            }
        });
    }

    private void setImage(JLabel label) {
        URL imageUrl = getClass().getResource("/images/access-control.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(image));
        } else {
            System.err.println("Image not found: " + "/images/access-control.png");
        }
    }
}
