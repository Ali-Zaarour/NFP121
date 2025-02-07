package dev.alizaarour.views.components;

import dev.alizaarour.services.UserService;

import javax.swing.*;

public class EditModal extends JDialog {
    private final JTextField inputField;
    private final String fieldToEdit;

    public EditModal(String title, String field) {
        this.fieldToEdit = field;

        setTitle(title);
        setSize(350, 150);
        setLayout(null);
        setLocationRelativeTo(null);

        int startX = 10;
        int startY = 20;
        int fieldWidth = 320;
        int buttonWidth = 320;
        int fieldHeight = 35;
        int gapY = 10;

        inputField = new JTextField();
        inputField.setBounds(startX, startY, fieldWidth, fieldHeight);
        add(inputField);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(startX, startY + fieldHeight + gapY, buttonWidth, fieldHeight);
        saveButton.addActionListener(e -> saveChanges());
        add(saveButton);

        setModal(true);
        setVisible(true);
    }

    private void saveChanges() {
        String newValue = inputField.getText().trim();
        if (newValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Value cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserService.getInstance().updateUserField(fieldToEdit, newValue)) {
            JOptionPane.showMessageDialog(this, "Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
