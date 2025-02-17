package dev.alizaarour.views.components;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.*;
import dev.alizaarour.services.CourseService;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherCreateCoursePage implements Page {
    private final JPanel mainPanel;
    private JTextField syllabusInput, levelInput, contentField, courseTitleField;
    private DefaultListModel<String> syllabusModel;
    private JList<String> syllabusList;
    private DefaultListModel<Level> levelModel;
    private JList<Level> levelList;
    private JSpinner feeSpinner;
    private DefaultTreeModel chapterTreeModel;
    private JTree chapterTree;
    private DefaultMutableTreeNode rootNode;
    private JComboBox<String> contentTypeBox;
    private JTextArea courseObjectiveField;

    public TeacherCreateCoursePage() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(1000, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; // Distribute width properly
        gbc.weighty = 1; // Allow components to expand

        createTopRow(gbc);  // Row 1: Course Info + Syllabus
        createMiddleRow(gbc); // Row 2: Levels
        createBottomRow(gbc); // Row 3: Chapters & Content
        addSaveButton(gbc); // Save Button

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    // ----------- ROW 1: Course Info + Syllabus ---------------
    private void createTopRow(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weighty = 0.3; // Increased weight to allocate more height

        // -------------------- COURSE INFORMATION PANEL --------------------
        JPanel coursePanel = new JPanel(new GridBagLayout());
        coursePanel.setBorder(BorderFactory.createTitledBorder("Course Information"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        coursePanel.add(new JLabel("Course Title:"), c);

        c.gridx = 1;
        c.weightx = 0.7;
        courseTitleField = new JTextField(25);
        coursePanel.add(courseTitleField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.3;
        coursePanel.add(new JLabel("Created By:"), c);

        c.gridx = 1;
        c.weightx = 0.7;
        JTextField createdByField = new JTextField(SessionManager.getInstance().getUser().getEmail(), 25);
        createdByField.setEditable(false);
        coursePanel.add(createdByField, c);

        // -------------------- FIXED COURSE OBJECTIVE TEXT AREA --------------------
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.3;
        c.weighty = 0.5; // Allocate vertical space for expansion
        c.fill = GridBagConstraints.BOTH;
        coursePanel.add(new JLabel("Course Objective:"), c);

        c.gridx = 1;
        c.weightx = 0.7;
        c.gridwidth = 2;
        c.gridheight = 2; // Span multiple rows to ensure height
        c.weighty = 1.0; // Make sure it expands properly

        courseObjectiveField = new JTextArea(10, 40); // Large area
        courseObjectiveField.setLineWrap(true);
        courseObjectiveField.setWrapStyleWord(true);

        JScrollPane objectiveScrollPane = new JScrollPane(courseObjectiveField);
        coursePanel.add(objectiveScrollPane, c);

        mainPanel.add(coursePanel, gbc);

        // -------------------- COURSE SYLLABUS SECTION --------------------
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        JPanel syllabusPanel = new JPanel(new BorderLayout());
        syllabusPanel.setBorder(BorderFactory.createTitledBorder("Course Syllabus"));

        syllabusInput = new JTextField(20);
        JButton addSyllabusButton = new JButton("Add");
        addSyllabusButton.addActionListener(e -> addSyllabusItem());

        JButton removeSyllabusButton = new JButton("Remove");
        removeSyllabusButton.addActionListener(e -> removeSelectedSyllabusItem());

        syllabusModel = new DefaultListModel<>();
        syllabusList = new JList<>(syllabusModel);
        JScrollPane syllabusScroll = new JScrollPane(syllabusList);

        JPanel syllabusInputPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        syllabusInputPanel.add(syllabusInput);
        syllabusInputPanel.add(addSyllabusButton);
        syllabusInputPanel.add(removeSyllabusButton);

        syllabusPanel.add(syllabusInputPanel, BorderLayout.NORTH);
        syllabusPanel.add(syllabusScroll, BorderLayout.CENTER);

        mainPanel.add(syllabusPanel, gbc);
    }

    // ----------- ROW 2: Levels ---------------
    private void createMiddleRow(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.4; // Increased weight to allow more height

        JPanel levelPanel = new JPanel(new BorderLayout());
        levelPanel.setBorder(BorderFactory.createTitledBorder("Levels"));

        levelModel = new DefaultListModel<>();
        levelList = new JList<>(levelModel);
        levelList.setFixedCellHeight(30);

        levelList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadChaptersForLevel();
            }
        });

        // Ensure the scroll pane gets enough height
        JScrollPane levelScroll = new JScrollPane(levelList);
        levelScroll.setPreferredSize(new Dimension(300, 200)); // Increased height

        JPanel levelInputPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        levelInput = new JTextField();
        feeSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 10.0));
        JButton addLevelBtn = new JButton("Add");
        addLevelBtn.addActionListener(e -> addLevel());

        JButton removeLevelBtn = new JButton("Remove");
        removeLevelBtn.addActionListener(e -> removeSelectedLevel());

        levelInputPanel.add(levelInput);
        levelInputPanel.add(feeSpinner);
        levelInputPanel.add(addLevelBtn);

        levelPanel.add(levelInputPanel, BorderLayout.NORTH);
        levelPanel.add(levelScroll, BorderLayout.CENTER);
        levelPanel.add(removeLevelBtn, BorderLayout.SOUTH);

        mainPanel.add(levelPanel, gbc);
    }

    // ----------- ROW 3: Chapters & Content ---------------
    private void createBottomRow(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.6;

        // Create a parent panel with GridBagLayout (2 columns: 60% for a tree, 40% for content)
        JPanel chapterAndContentPanel = new JPanel(new GridBagLayout());
        chapterAndContentPanel.setBorder(BorderFactory.createTitledBorder("Chapters & Content"));

        GridBagConstraints subGBC = new GridBagConstraints();
        subGBC.insets = new Insets(5, 5, 5, 5);
        subGBC.fill = GridBagConstraints.BOTH;
        subGBC.weighty = 1;

        // ----------- CHAPTER TREE PANEL (60%) -----------
        subGBC.gridx = 0;
        subGBC.gridy = 0;
        subGBC.weightx = 0.6; // 60% of the width

        JPanel chapterPanel = new JPanel(new BorderLayout());
        chapterPanel.setBorder(BorderFactory.createTitledBorder("Chapters"));

        rootNode = new DefaultMutableTreeNode("Chapters");
        chapterTreeModel = new DefaultTreeModel(rootNode);
        chapterTree = new JTree(chapterTreeModel);
        JScrollPane chapterScroll = new JScrollPane(chapterTree);
        chapterScroll.setPreferredSize(new Dimension(700, 300));

        JButton addChapterBtn = new JButton("Add Chapter");
        addChapterBtn.addActionListener(e -> addChapter());

        JButton addSubChapterBtn = new JButton("Add Sub-Chapter");
        addSubChapterBtn.addActionListener(e -> addSubChapter());

        JPanel chapterButtonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chapterButtonPanel.add(addChapterBtn);
        chapterButtonPanel.add(addSubChapterBtn);

        chapterPanel.add(chapterScroll, BorderLayout.CENTER);
        chapterPanel.add(chapterButtonPanel, BorderLayout.SOUTH);

        chapterAndContentPanel.add(chapterPanel, subGBC);

        // ----------- CHAPTER CONTENT PANEL (40%) -----------
        subGBC.gridx = 1;
        subGBC.weightx = 0.4; // 40% of the width

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder("Chapter Content"));

        GridBagConstraints contentGBC = new GridBagConstraints();
        contentGBC.insets = new Insets(5, 5, 5, 5);
        contentGBC.fill = GridBagConstraints.HORIZONTAL;
        contentGBC.gridx = 0;
        contentGBC.gridy = 0;
        contentGBC.weightx = 1;
        contentGBC.gridwidth = 2; // Full width

        contentPanel.add(new JLabel("Content Type:"), contentGBC);

        contentGBC.gridy++;
        contentTypeBox = new JComboBox<>(new String[]{"Video", "Voice", "Image", "Document"});
        contentPanel.add(contentTypeBox, contentGBC);

        contentGBC.gridy++;
        contentPanel.add(new JLabel("Content Description:"), contentGBC);

        contentGBC.gridy++;
        contentField = new JTextField();
        contentPanel.add(contentField, contentGBC);

        contentGBC.gridy++;
        JButton addContentBtn = new JButton("Add Content");
        addContentBtn.addActionListener(e -> addContent());
        contentPanel.add(addContentBtn, contentGBC);

        chapterAndContentPanel.add(contentPanel, subGBC);

        // Add the combined panel to the main layout
        mainPanel.add(chapterAndContentPanel, gbc);
    }

    private void addSaveButton(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;

        JButton saveButton = new JButton("Save Course");
        saveButton.addActionListener(e -> saveCourse());
        mainPanel.add(saveButton, gbc);
    }

    // ----------- METHODS: Add Levels, Chapters, Content ---------------
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

    private void addLevel() {
        String title = levelInput.getText().trim();
        double fee = (double) feeSpinner.getValue();
        if (!title.isEmpty()) {
            Level newLevel = new Level(levelModel.getSize() + 1, title, fee, new ArrayList<>(), null);
            levelModel.addElement(newLevel);
            levelInput.setText("");
        }
    }

    private void removeSelectedLevel() {
        int selectedIndex = levelList.getSelectedIndex();
        if (selectedIndex != -1) {
            levelModel.remove(selectedIndex);
            rootNode.removeAllChildren(); // Clear chapters from UI
            chapterTreeModel.reload();
        }
    }

    private void addChapter() {
        Level selectedLevel = levelList.getSelectedValue();
        if (selectedLevel == null) {
            JOptionPane.showMessageDialog(mainPanel, "Select a level first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String chapterTitle = JOptionPane.showInputDialog("Enter Chapter Title:");
        if (chapterTitle != null && !chapterTitle.trim().isEmpty()) {
            Chapter newChapter = new Chapter(chapterTitle);
            selectedLevel.addChapter(newChapter); // Store in Level

            // Add chapter to a UI tree
            DefaultMutableTreeNode newChapterNode = new DefaultMutableTreeNode(newChapter.getTitle());
            rootNode.add(newChapterNode);
            chapterTreeModel.reload();
        }
    }

    private void addSubChapter() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) chapterTree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            String subChapterTitle = JOptionPane.showInputDialog("Enter Sub-Chapter Title:");
            if (subChapterTitle != null && !subChapterTitle.trim().isEmpty()) {
                selectedNode.add(new DefaultMutableTreeNode(subChapterTitle));
                chapterTreeModel.reload();
            }
        }
    }

    private void addContent() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) chapterTree.getLastSelectedPathComponent();

        if (selectedNode != null && !selectedNode.isRoot()) {
            String contentValue = contentField.getText().trim();
            ContentType selectedType = ContentType.valueOf(contentTypeBox.getSelectedItem().toString().toUpperCase());

            if (!contentValue.isEmpty()) {
                // Use Factory to create the content object
                CourseContent newContent = CourseContentFactory.createContent(selectedType, contentValue);

                // Store content inside the selected Chapter
                Chapter selectedChapter = findChapterByTitle(selectedNode.toString());
                if (selectedChapter != null) {
                    selectedChapter.getContents().add(newContent); // Save inside the Chapter
                }

                // Add to a UI tree
                DefaultMutableTreeNode contentNode = new DefaultMutableTreeNode(newContent.getContent());
                selectedNode.add(contentNode);
                chapterTreeModel.reload();

                contentField.setText(""); // Clear input field
            }
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Please select a chapter to add content.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Chapter findChapterByTitle(String title) {
        Level selectedLevel = levelList.getSelectedValue();
        if (selectedLevel != null) {
            for (Chapter chapter : selectedLevel.getChapters()) {
                if (chapter.getTitle().equals(title)) {
                    return chapter;
                }
            }
        }
        return null;
    }

    private void loadChaptersForLevel() {
        Level selectedLevel = levelList.getSelectedValue();
        rootNode.removeAllChildren(); // Clear previous chapters

        if (selectedLevel != null) {
            for (Chapter chapter : selectedLevel.getChapters()) {
                DefaultMutableTreeNode chapterNode = new DefaultMutableTreeNode(chapter.getTitle());
                rootNode.add(chapterNode);
            }
        }
        chapterTreeModel.reload();
    }

    private void saveCourse() {
        // Get course title and objective
        String courseTitle = courseTitleField.getText().trim();
        String courseObjective = courseObjectiveField.getText().trim();
        String createdBy = SessionManager.getInstance().getUser().getEmail();

        // Validate input
        if (courseTitle.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter a course title.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new Course object
        Course newCourse = new Course(courseTitle, createdBy, courseObjective);

        // Set the syllabus
        List<String> syllabusData = new ArrayList<>();
        for (int i = 0; i < syllabusModel.size(); i++) {
            syllabusData.add(syllabusModel.get(i));
        }
        newCourse.setSyllabus(syllabusData); // Store syllabus properly

        // Add all levels to the course
        for (int i = 0; i < levelModel.size(); i++) {
            Level level = levelModel.get(i);
            newCourse.getLevels().add(level);
        }

        // Simulate saving to a database (replace with actual DB logic if needed)
        System.out.println("Course Saved: " + newCourse);
        if (ApplicationInitializer.dataSchema.getCourses() == null) {
            ApplicationInitializer.dataSchema.setCourses(new ArrayList<>());
        }

        CourseService.getInstance().addCourse(newCourse);

        // Show a success message
        JOptionPane.showMessageDialog(mainPanel, "Done! Course has been saved.", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear all components to allow a new course creation
        clearAllFields();
    }

    private void clearAllFields() {
        courseTitleField.setText("");
        courseObjectiveField.setText("");

        syllabusModel.clear();
        syllabusInput.setText("");

        levelModel.clear();
        levelInput.setText("");
        feeSpinner.setValue(100.0);

        rootNode.removeAllChildren(); // Clear the Chapter Tree
        chapterTreeModel.reload();

        contentField.setText("");
    }

    @Override
    public JPanel getPagePanel() {
        return mainPanel;
    }
}
