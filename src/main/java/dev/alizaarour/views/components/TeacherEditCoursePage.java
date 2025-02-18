package dev.alizaarour.views.components;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.models.*;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.views.BaseFrame;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherEditCoursePage extends BaseFrame {

    private JPanel containerPanel, mainPanel;
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
    private Course course; // The course being edited
    private int courseId;
    private DefaultListModel<String> quizModel;

    public TeacherEditCoursePage(int courseId) {
        super("update course", 1000, 800, false);

        this.courseId = courseId; // Store the existing course
        loadData(); // Load course data

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    @Override
    protected void createComponents() {
        mainPanel = new JPanel(new GridBagLayout()); // Keep using GridBagLayout
        mainPanel.setPreferredSize(new Dimension(900, 1200)); // Ensure enough space

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH; // Allow components to expand
        gbc.weightx = 1; // Ensures even column distribution
        gbc.weighty = 0; // Prevents stretching

        // --------------- ROW 1: Course Info & Syllabus (Two Columns) -----------------
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // First column
        gbc.weightx = 0.5;
        createTopRow(gbc); // Course Info

        gbc.gridx = 1;
        createSyllabusRow(gbc); // Syllabus on the right

        // --------------- ROW 2: Levels (Full Width) -----------------
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Levels should span the full width
        gbc.weightx = 1;
        createMiddleRow(gbc);

        // --------------- ROW 3: Chapters & Content (Full Width) -----------------
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Spanning full width
        createBottomRow(gbc);

        // --------------- ROW 4: Quiz (Full Width) -----------------
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        createQuizRow(gbc);

        // --------------- ROW 5: Save Button (Centered) -----------------
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addUpdateButton(gbc);

        // Wrap `mainPanel` inside a `JScrollPane`
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add the scroll pane to a container panel for proper layout
        containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(containerPanel);
    }

    // ----------- ROW 1: Course Info + Syllabus ---------------
    private void createTopRow(GridBagConstraints gbc) {
        JPanel coursePanel = new JPanel(new GridBagLayout());
        coursePanel.setBorder(BorderFactory.createTitledBorder("Course Information"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH; // Ensure expansion
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1; // Reduce label width allocation
        c.anchor = GridBagConstraints.WEST; // Align labels to the left

        coursePanel.add(new JLabel("Course Title:"), c);

        c.gridx = 1;
        c.weightx = 0.9; // Allocate most width to text field
        courseTitleField = new JTextField(25);
        courseTitleField.setPreferredSize(new Dimension(350, 40)); // Increased width
        coursePanel.add(courseTitleField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        coursePanel.add(new JLabel("Created By:"), c);

        c.gridx = 1;
        c.weightx = 0.9;
        JTextField createdByField = new JTextField(SessionManager.getInstance().getUser().getEmail(), 25);
        createdByField.setEditable(false);
        createdByField.setPreferredSize(new Dimension(350, 40)); // Match courseTitleField width
        coursePanel.add(createdByField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.1;
        coursePanel.add(new JLabel("Course Objective:"), c);

        c.gridx = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.9; // Give more space to objective text area
        c.weighty = 1.0;

        courseObjectiveField = new JTextArea(8, 40); // Adjusted size
        courseObjectiveField.setLineWrap(true);
        courseObjectiveField.setWrapStyleWord(true);

        JScrollPane objectiveScrollPane = new JScrollPane(courseObjectiveField);
        objectiveScrollPane.setPreferredSize(new Dimension(350, 140)); // Increased width & height

        coursePanel.add(objectiveScrollPane, c);

        gbc.weighty = 0.5; // Increase space allocation for this row
        mainPanel.add(coursePanel, gbc);
    }


    private void createSyllabusRow(GridBagConstraints gbc) {
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
        syllabusScroll.setPreferredSize(new Dimension(300, 200)); // Increased height

        JPanel syllabusInputPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        syllabusInputPanel.add(syllabusInput);
        syllabusInputPanel.add(addSyllabusButton);
        syllabusInputPanel.add(removeSyllabusButton);

        syllabusPanel.add(syllabusInputPanel, BorderLayout.NORTH);
        syllabusPanel.add(syllabusScroll, BorderLayout.CENTER);

        gbc.weighty = 0.5; // Increased height for syllabus section
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

        JButton removeNodeBtn = new JButton("Remove");
        removeNodeBtn.addActionListener(e -> removeSelectedNode());

        JPanel chapterButtonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        chapterButtonPanel.add(addChapterBtn);
        chapterButtonPanel.add(addSubChapterBtn);
        chapterButtonPanel.add(removeNodeBtn); // Add the remove button

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

    private void createQuizRow(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;

        JPanel quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBorder(BorderFactory.createTitledBorder("Quizzes for Levels"));

        // List model for quizzes
        quizModel = new DefaultListModel<>();
        JList<String> quizList = new JList<>(quizModel);
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane quizScroll = new JScrollPane(quizList);
        quizScroll.setPreferredSize(new Dimension(400, 100));

        // Buttons for quiz management
        JButton createQuizBtn = new JButton("Create Quiz");
        createQuizBtn.addActionListener(e -> createQuiz(quizModel));

        JButton updateQuizBtn = new JButton("Update Quiz");
        updateQuizBtn.addActionListener(e -> updateQuiz());

        JButton removeQuizBtn = new JButton("Remove Quiz");
        removeQuizBtn.addActionListener(e -> removeQuiz(quizModel));

        JPanel quizButtonPanel = new JPanel(new FlowLayout());
        quizButtonPanel.add(createQuizBtn);
        quizButtonPanel.add(updateQuizBtn);
        quizButtonPanel.add(removeQuizBtn);

        quizPanel.add(quizScroll, BorderLayout.CENTER);
        quizPanel.add(quizButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(quizPanel, gbc);
    }

    private void addUpdateButton(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.PAGE_END;

        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton updateButton = new JButton("Update Course");
        updateButton.setPreferredSize(new Dimension(200, 40));
        updateButton.addActionListener(e -> updateCourse());

        updatePanel.add(updateButton);
        mainPanel.add(updatePanel, gbc);
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

    private void removeSelectedNode() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) chapterTree.getLastSelectedPathComponent();

        if (selectedNode != null && !selectedNode.isRoot()) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();

            // Confirm before deletion
            int confirm = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to delete this node?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (parent != null) {
                    parent.remove(selectedNode); // Remove from a tree
                } else {
                    rootNode.remove(selectedNode); // If no parent, remove from root
                }
                chapterTreeModel.reload();
            }
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Please select a node to remove.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void createQuiz(DefaultListModel<String> quizModel) {
        Level selectedLevel = levelList.getSelectedValue();
        if (selectedLevel == null) {
            JOptionPane.showMessageDialog(mainPanel, "Select a level first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the level already has a quiz
        if (selectedLevel.getQuiz() != null) {
            JOptionPane.showMessageDialog(mainPanel, "This level already has a quiz.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new quiz
        Quiz newQuiz = new Quiz();

        // Add 10 empty questions (to be edited by the user)
        for (int i = 0; i < 10; i++) {
            newQuiz.addQuestion(new Question("Question " + (i + 1), new ArrayList<>(), 0));
        }

        selectedLevel.setQuiz(newQuiz);
        quizModel.addElement("Quiz for Level: " + selectedLevel.getTitle());

        // Open quiz editor UI
        openQuizEditor(selectedLevel);
    }

    private void openQuizEditor(Level level) {
        Quiz quiz = level.getQuiz();
        if (quiz == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(mainPanel), "Edit Quiz for Level: " + level.getTitle(), true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        DefaultListModel<String> questionModel = new DefaultListModel<>();
        JList<String> questionList = new JList<>(questionModel);
        JScrollPane scrollPane = new JScrollPane(questionList);

        for (int i = 0; i < quiz.getQuestions().length; i++) {
            Question q = quiz.getQuestions()[i];
            if (q != null) {
                questionModel.addElement(q.getQuestionText() + " (" + q.getChoices().size() + " choices)");
            }
        }

        JButton editQuestionBtn = new JButton("Edit Question");
        editQuestionBtn.addActionListener(e -> {
            int selectedIndex = questionList.getSelectedIndex();
            if (selectedIndex >= 0) {
                editQuestion(quiz, selectedIndex, questionModel);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editQuestionBtn);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setVisible(true);
    }

    private void editQuestion(Quiz quiz, int index, DefaultListModel<String> questionModel) {
        if (index < 0 || index >= 10) {
            JOptionPane.showMessageDialog(null, "Select a valid question to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Question question = quiz.getQuestions()[index];
        if (question == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(mainPanel), "Edit Question " + (index + 1), true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        JTextField questionField = new JTextField(question.getQuestionText());
        DefaultListModel<String> choiceModel = new DefaultListModel<>();
        JList<String> choiceList = new JList<>(choiceModel);

        // Load choices, marking the correct one
        for (int i = 0; i < question.getChoices().size(); i++) {
            String choiceText = question.getChoices().get(i);
            if (i == question.getCorrectChoiceIndex()) {
                choiceText += " ✅";  // Add icon to correct answer
            }
            choiceModel.addElement(choiceText);
        }

        JButton addChoiceBtn = new JButton("Add Choice");
        addChoiceBtn.addActionListener(e -> {
            String newChoice = JOptionPane.showInputDialog("Enter new choice:");
            if (newChoice != null && !newChoice.trim().isEmpty()) {
                choiceModel.addElement(newChoice);
            }
        });

        JButton setCorrectBtn = new JButton("Set Correct Answer");
        setCorrectBtn.addActionListener(e -> {
            int selectedChoice = choiceList.getSelectedIndex();
            if (selectedChoice >= 0) {
                java.util.List<String> choices = new ArrayList<>();
                for (int i = 0; i < choiceModel.getSize(); i++) {
                    choices.add(choiceModel.getElementAt(i).replace(" ✅", "")); // Remove old icon

                    if (i == selectedChoice) {
                        choices.set(i, choices.get(i) + " ✅"); // Add ✅ to new correct choice
                    }
                }

                // Update the question
                quiz.getQuestions()[index] = new Question(
                        questionField.getText(),
                        choices,
                        selectedChoice
                );

                choiceModel.clear();
                for (String choice : choices) {
                    choiceModel.addElement(choice);
                }

                JOptionPane.showMessageDialog(null, "Correct answer set!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton saveQuestionBtn = new JButton("Save Question");
        saveQuestionBtn.addActionListener(e -> {
            // Convert DefaultListModel to List<String>
            java.util.List<String> choices = new ArrayList<>();
            for (int i = 0; i < choiceModel.getSize(); i++) {
                choices.add(choiceModel.getElementAt(i));
            }

            // Save new question text
            quiz.getQuestions()[index] = new Question(
                    questionField.getText(),
                    choices,
                    quiz.getQuestions()[index].getCorrectChoiceIndex()
            );

            // Update the quiz editor UI immediately with the new title
            questionModel.set(index, questionField.getText() + " (" + choices.size() + " choices)");

            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addChoiceBtn);
        buttonPanel.add(setCorrectBtn);
        buttonPanel.add(saveQuestionBtn);

        dialog.add(questionField, BorderLayout.NORTH);
        dialog.add(new JScrollPane(choiceList), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setVisible(true);
    }

    private void updateQuiz() {
        Level selectedLevel = levelList.getSelectedValue();
        if (selectedLevel == null) {
            JOptionPane.showMessageDialog(mainPanel, "Select a level first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Quiz quiz = selectedLevel.getQuiz();
        if (quiz == null) {
            JOptionPane.showMessageDialog(mainPanel, "No quiz found for this level.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open quiz editor
        openQuizEditor(selectedLevel);
    }

    private void removeQuiz(DefaultListModel<String> quizModel) {
        Level selectedLevel = levelList.getSelectedValue();
        if (selectedLevel == null) {
            JOptionPane.showMessageDialog(mainPanel, "Select a level first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedLevel.getQuiz() == null) {
            JOptionPane.showMessageDialog(mainPanel, "No quiz found for this level.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to remove this quiz?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            selectedLevel.setQuiz(null);
            quizModel.removeAllElements();
            JOptionPane.showMessageDialog(mainPanel, "Quiz removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateCourse() {
        String updatedTitle = courseTitleField.getText().trim();
        String updatedObjective = courseObjectiveField.getText().trim();

        if (updatedTitle.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter a course title.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update existing `course` instead of creating a new one
        course.setTitle(updatedTitle);
        course.setObjective(updatedObjective);

        // Update syllabus
        List<String> updatedSyllabus = new ArrayList<>();
        for (int i = 0; i < syllabusModel.size(); i++) {
            updatedSyllabus.add(syllabusModel.get(i));
        }
        course.setSyllabus(updatedSyllabus);

        // Update levels
        List<Level> updatedLevels = new ArrayList<>();
        for (int i = 0; i < levelModel.size(); i++) {
            updatedLevels.add(levelModel.get(i));
        }
        course.setLevels(updatedLevels);

        // Update course in the database
        CourseService.getInstance().updateCourse(courseId, course);

        // Save changes
        JOptionPane.showMessageDialog(mainPanel, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh UI if needed
        dispose(); // Close the editor after updating
    }

    // ----------- INITIALIZATION ---------------
    private void loadData(){
        this.course = CourseService.getInstance().getCourseById(courseId);

        // Fill title and objective
        courseTitleField.setText(course.getTitle());
        courseObjectiveField.setText(course.getObjective());

        // Populate syllabus list
        syllabusModel.clear();
        for (String item : course.getSyllabus()) {
            syllabusModel.addElement(item);
        }

        // Populate levels
        levelModel.clear();
        for (Level level : course.getLevels()) {
            levelModel.addElement(level);
        }

        // Populate Quizzes
        quizModel.clear(); // Clear previous items before reloading
        for (Level level : course.getLevels()) {
            if (level.getQuiz() != null) {
                quizModel.addElement("Quiz for Level: " + level.getTitle());
            }
        }


    }

    @Override
    protected void initVariable() {
    }
}
