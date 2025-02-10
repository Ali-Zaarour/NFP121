package dev.alizaarour.views.components;

import dev.alizaarour.config.SessionManager;

import javax.swing.*;
import java.awt.*;

public class TeacherCreateCoursePage implements Page {
    private final JPanel mainPanel;
    private JTextField titleField, syllabusInput;
    private JTextArea objectiveField;
    private DefaultListModel<String> syllabusModel;
    private JList<String> syllabusList;
    private JButton addSyllabusButton, removeSyllabusButton;

    public TeacherCreateCoursePage() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null); // Manual positioning
        mainPanel.setPreferredSize(new Dimension(1000, 500));

        createCourseDetails(); // 🔥 Encapsulated UI setup

        // Wrap in ScrollPane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    private void createCourseDetails() {
        int startXLeft = 50;
        int startXRight = 550;
        int startY = 30;
        int labelWidth = 150;
        int fieldWidth = 350;
        int fieldHeight = 35;
        int buttonWidth = 50;
        int listWidth = 350;
        int gapY = 50;

        // Title Label
        JLabel titleLabel = new JLabel("Create New Course");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(50, 10, 300, fieldHeight);
        mainPanel.add(titleLabel);

        // Main Separator
        JSeparator mainSeparator = new JSeparator();
        mainSeparator.setBounds(50, 50, 900, 5);
        mainPanel.add(mainSeparator);

        // Left Side Elements
        JLabel courseTitleLabel = new JLabel("Course Title:");
        courseTitleLabel.setBounds(startXLeft, startY, labelWidth, fieldHeight);
        mainPanel.add(courseTitleLabel);

        titleField = new JTextField();
        titleField.setBounds(startXLeft + labelWidth, startY, fieldWidth, fieldHeight);
        mainPanel.add(titleField);

        JLabel createdByLabel = new JLabel("Created By:");
        createdByLabel.setBounds(startXLeft, startY + gapY, labelWidth, fieldHeight);
        mainPanel.add(createdByLabel);

        JTextField createdByField = new JTextField(SessionManager.getInstance().getUser().getEmail());
        createdByField.setBounds(startXLeft + labelWidth, startY + gapY, fieldWidth, fieldHeight);
        createdByField.setEditable(false);
        mainPanel.add(createdByField);

        JLabel objectiveLabel = new JLabel("Objective:");
        objectiveLabel.setBounds(startXLeft, startY + 2 * gapY, labelWidth, fieldHeight);
        mainPanel.add(objectiveLabel);

        objectiveField = new JTextArea(3, 20);
        JScrollPane objectiveScroll = new JScrollPane(objectiveField);
        objectiveScroll.setBounds(startXLeft + labelWidth, startY + 2 * gapY, fieldWidth, 80);
        mainPanel.add(objectiveScroll);

        // Left Section Separator
        JSeparator leftSeparator = new JSeparator();
        leftSeparator.setBounds(startXLeft, startY + 3 * gapY + 90, fieldWidth + labelWidth, 5);
        mainPanel.add(leftSeparator);

        // Right Side Elements (Syllabus Section)
        JLabel syllabusLabel = new JLabel("Syllabus:");
        syllabusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        syllabusLabel.setBounds(startXRight, startY, listWidth, fieldHeight);
        mainPanel.add(syllabusLabel);

        syllabusInput = new JTextField();
        syllabusInput.setBounds(startXRight, startY + gapY, listWidth - (2 * buttonWidth) - 10, fieldHeight);
        mainPanel.add(syllabusInput);

        addSyllabusButton = new JButton("+");
        addSyllabusButton.setBounds(startXRight + listWidth - (2 * buttonWidth), startY + gapY, buttonWidth, fieldHeight);
        addSyllabusButton.addActionListener(e -> addSyllabusItem());
        mainPanel.add(addSyllabusButton);

        removeSyllabusButton = new JButton("X");
        removeSyllabusButton.setBounds(startXRight + listWidth - buttonWidth, startY + gapY, buttonWidth, fieldHeight);
        removeSyllabusButton.addActionListener(e -> removeSelectedSyllabusItem());
        mainPanel.add(removeSyllabusButton);

        syllabusModel = new DefaultListModel<>();
        syllabusList = new JList<>(syllabusModel);
        syllabusList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane syllabusScroll = new JScrollPane(syllabusList);
        syllabusScroll.setBounds(startXRight, startY + 2 * gapY, listWidth, 150);
        mainPanel.add(syllabusScroll);

        // Right Section Separator
        JSeparator rightSeparator = new JSeparator();
        rightSeparator.setBounds(startXRight, startY + 3 * gapY + 90, listWidth, 5);
        mainPanel.add(rightSeparator);
    }

    private void addSyllabusItem() {
        String text = syllabusInput.getText().trim();
        if (!text.isEmpty()) {
            syllabusModel.addElement(text);
            syllabusInput.setText("");
        }
    }

    private void removeSelectedSyllabusItem() {
        int selectedIndex = syllabusList.getSelectedIndex();
        if (selectedIndex != -1) {
            syllabusModel.remove(selectedIndex);
        }
    }

    @Override
    public JPanel getPagePanel() {
        return mainPanel;
    }
}
