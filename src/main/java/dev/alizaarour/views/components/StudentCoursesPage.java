package dev.alizaarour.views.components;

import dev.alizaarour.models.*;
import dev.alizaarour.services.CourseProcessService;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.PaymentService;
import dev.alizaarour.services.UserService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentCoursesPage implements Page, Observer {
    private JPanel mainPanel;
    private JPanel coursesContainer;

    public StudentCoursesPage() {
        PaymentService.getInstance().addObserver(this);
        CourseProcessService.getInstance().addObserver(this);
        init();
    }

    @Override
    public JPanel getPagePanel() {
        return mainPanel;
    }

    @Override
    public void update() {
        loadCourses();
    }

    private void init() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        //mainPanel.setBackground(null);

        JLabel titleLabel = new JLabel("My Enrolled Courses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Scrollable Container for Courses (Ensures Left Alignment)
        coursesContainer = new JPanel();
        coursesContainer.setLayout(new BoxLayout(coursesContainer, BoxLayout.Y_AXIS));
        coursesContainer.setOpaque(false);
        coursesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        coursesContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // No margin or padding

        // Wrap the container inside a panel that enforces left alignment
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.add(coursesContainer);
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add to the main panel
        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        loadCourses();
    }

    private void loadCourses() {
        coursesContainer.removeAll();

        User activeUser = UserService.getInstance().getActiveUser();
        if (!(activeUser instanceof Student student)) {
            return;
        }

        List<CourseProcess> enrolledCourses = student.getCourseProcesses();
        for (CourseProcess process : enrolledCourses) {
            JPanel coursePanel = createCoursePanel(process);

            coursesContainer.add(coursePanel);

            // Create and store the vertical gap component (initially visible)
            Component verticalGap = Box.createVerticalStrut(5);
            coursesContainer.add(verticalGap);

            // Store the reference to the gap component in the panel
            coursePanel.putClientProperty("gap", verticalGap);
        }

        coursesContainer.revalidate();
        coursesContainer.repaint();
    }

    private JPanel createCoursePanel(CourseProcess process) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        panel.setBackground(Color.WHITE);

        // Set fixed height for the collapsed course panel
        int panelWidth = 920; // Adjust width as needed
        int collapsedHeight = 50; // Height when collapsed
        int expandedHeight = 180; // Height when expanded (to fit goal)

        panel.setPreferredSize(new Dimension(panelWidth, collapsedHeight));
        panel.setMaximumSize(new Dimension(panelWidth, collapsedHeight));
        panel.setMinimumSize(new Dimension(panelWidth, collapsedHeight));

        // Fetch course details
        var course = CourseService.getInstance().getCourseById(process.getCourseId());
        String courseTitle = (course != null) ? course.getTitle() + " - Level " + course.getLevels().get(process.getLevelId() - 1).getTitle() : "Unknown Course";
        String courseObjective = (course != null) ? course.getObjective() : "No Objective Available";

        // --- Title Row (With Arrow) ---
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(12, 15, 12, 15));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(courseTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel arrowLabel = new JLabel("▼");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 14));
        arrowLabel.setForeground(Color.GRAY);

        JLabel stateLabel = new JLabel(process.getState().getName(), SwingConstants.RIGHT);
        stateLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        stateLabel.setForeground(Color.DARK_GRAY);

        JPanel titleLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleLeftPanel.setOpaque(false);
        titleLeftPanel.add(arrowLabel);
        titleLeftPanel.add(Box.createHorizontalStrut(8));
        titleLeftPanel.add(titleLabel);

        titlePanel.add(titleLeftPanel, BorderLayout.WEST);
        titlePanel.add(stateLabel, BorderLayout.EAST);

        // --- Course Details Panel (Initially Hidden) ---
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        detailsPanel.setBackground(new Color(240, 240, 240));

        // Objective Text
        JTextArea objectiveArea = new JTextArea(courseObjective, 3, 40);
        objectiveArea.setFont(new Font("Arial", Font.PLAIN, 13));
        objectiveArea.setWrapStyleWord(true);
        objectiveArea.setLineWrap(true);
        objectiveArea.setEditable(false);
        objectiveArea.setOpaque(false);
        objectiveArea.setBorder(null);
        objectiveArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JScrollPane objectiveScroll = new JScrollPane(objectiveArea);
        objectiveScroll.setBorder(null);
        objectiveScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        detailsPanel.add(objectiveScroll);
        detailsPanel.add(Box.createVerticalStrut(10));

        // Buttons Row (Right Aligned)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton quizButton = createStyledButton("Take a Quiz");
        JButton contentButton = createStyledButton("Course Content");

        quizButton.addActionListener(e -> {
            if (course != null && process.getLevelId() > 0 && process.getLevelId() <= course.getLevels().size()) {
                var level = course.getLevels().get(process.getLevelId() - 1); // Get the correct level

                if (level != null && level.getQuiz() != null) {
                    openQuizDialog(level.getQuiz(), process.getProcessId()); // Open quiz
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Quiz not available for this level!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Invalid level selection!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        contentButton.addActionListener(e -> {
            CourseProcessService.getInstance().nextState(process.getProcessId());
            openCourseContentDialog(process);
        });

        buttonPanel.add(quizButton);
        buttonPanel.add(contentButton);
        detailsPanel.add(buttonPanel);

        // --- Expand/Collapse Action ---
        titlePanel.addMouseListener(new MouseAdapter() {
            private boolean isOpen = false;

            @Override
            public void mouseClicked(MouseEvent evt) {
                isOpen = !isOpen;

                if (isOpen) {
                    // Expand: Increase panel height and add details
                    panel.setPreferredSize(new Dimension(panelWidth, expandedHeight));
                    panel.setMaximumSize(new Dimension(panelWidth, expandedHeight));
                    panel.add(detailsPanel, BorderLayout.CENTER);
                } else {
                    // Collapse: Reset panel height and remove details
                    panel.setPreferredSize(new Dimension(panelWidth, collapsedHeight));
                    panel.setMaximumSize(new Dimension(panelWidth, collapsedHeight));
                    panel.remove(detailsPanel);
                }

                arrowLabel.setText(isOpen ? "▲" : "▼");

                // Refresh layout
                panel.revalidate();
                panel.repaint();
                coursesContainer.revalidate();
                coursesContainer.repaint();
            }
        });

        panel.add(titlePanel, BorderLayout.NORTH);

        return panel;
    }


    /**
     * Creates a modern-styled button.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 150, 250));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void openQuizDialog(Quiz quiz, int processId) {
        JDialog quizDialog = new JDialog((Frame) null, "Take the Quiz", true);
        quizDialog.setLayout(new BorderLayout(10, 10));
        quizDialog.setSize(500, 600);
        quizDialog.setLocationRelativeTo(null);

        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));

        // Store user answers
        ButtonGroup[] answerGroups = new ButtonGroup[quiz.getQuestions().length];
        JRadioButton[][] answerButtons = new JRadioButton[quiz.getQuestions().length][];

        for (int i = 0; i < quiz.getQuestions().length; i++) {
            Question question = quiz.getQuestions()[i];
            if (question == null) continue;

            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            questionPanel.setBorder(BorderFactory.createTitledBorder("")); // Remove title border

            // ✅ Ensure question text takes full width
            JLabel questionLabel = new JLabel("<html><b>" + question.getQuestionText() + "</b></html>");
            questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            questionLabel.setMaximumSize(new Dimension(quizDialog.getWidth() - 40, Integer.MAX_VALUE));

            questionPanel.add(questionLabel);

            answerGroups[i] = new ButtonGroup();
            answerButtons[i] = new JRadioButton[question.getChoices().size()];

            for (int j = 0; j < question.getChoices().size(); j++) {
                String choiceText = question.getChoices().get(j);

                // ✅ Explicitly ensure the correct answer is not marked
                if (choiceText.contains("✅")) {
                    choiceText = choiceText.replace("✅", ""); // Remove checkmark
                }

                JRadioButton choiceButton = new JRadioButton(choiceText);
                answerGroups[i].add(choiceButton);
                questionPanel.add(choiceButton);
                answerButtons[i][j] = choiceButton;
            }

            quizPanel.add(questionPanel);
        }

        JScrollPane scrollPane = new JScrollPane(quizPanel);
        quizDialog.add(scrollPane, BorderLayout.CENTER);

        // Finish Button
        JButton finishButton = new JButton("Finish Quiz");
        finishButton.addActionListener(e -> {
            if (!allQuestionsAnswered(answerGroups)) {
                JOptionPane.showMessageDialog(quizDialog, "Please answer all questions before submitting!", "Warning", JOptionPane.WARNING_MESSAGE);
                return; // Prevent closing if not all are answered
            }

            int score = calculateQuizScore(quiz, answerButtons);
            CourseProcessService.getInstance().saveTheNewResult(processId, score);
            JOptionPane.showMessageDialog(quizDialog, "Your score: " + score + "/20");
            quizDialog.dispose();
        });

        quizDialog.add(finishButton, BorderLayout.SOUTH);
        quizDialog.setVisible(true);
    }

    private int calculateQuizScore(Quiz quiz, JRadioButton[][] answerButtons) {
        int score = 0;

        for (int i = 0; i < quiz.getQuestions().length; i++) {
            Question question = quiz.getQuestions()[i];
            if (question == null) continue;

            int correctIndex = question.getCorrectChoiceIndex();

            // Each correct answer adds 2 points
            if (answerButtons[i][correctIndex].isSelected()) {
                score += 2;
            }
        }

        return score;
    }

    private boolean allQuestionsAnswered(ButtonGroup[] answerGroups) {
        for (ButtonGroup group : answerGroups) {
            if (group.getSelection() == null) {
                return false; // If any question has no answer, return false
            }
        }
        return true;
    }

    private void openCourseContentDialog(CourseProcess process) {
        // Get the course details
        var course = CourseService.getInstance().getCourseById(process.getCourseId());
        if (course == null) {
            JOptionPane.showMessageDialog(mainPanel, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create dialog
        JDialog contentDialog = new JDialog((Frame) null, "Course Content - " + course.getTitle(), true);
        contentDialog.setSize(500, 600);
        contentDialog.setLayout(new BorderLayout(10, 10));
        contentDialog.setLocationRelativeTo(null);

        // Root node for tree
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Chapters");
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree chapterTree = new JTree(treeModel);

        for (Chapter chapter : course.getLevels().get(process.getLevelId() - 1).getChapters()) {
            DefaultMutableTreeNode chapterNode = new DefaultMutableTreeNode(chapter.getTitle());
            rootNode.add(chapterNode);
            addChapterContentRecursive(chapter, chapterNode); // ✅ Fixed recursive method
        }


        // Expand tree by default
        for (int i = 0; i < chapterTree.getRowCount(); i++) {
            chapterTree.expandRow(i);
        }

        chapterTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Double-click detection
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) chapterTree.getLastSelectedPathComponent();
                    if (selectedNode == null) return;

                    String selectedText = selectedNode.toString(); // Get node name

                    // Search for the object by name
                    Object selectedObject = findChapterOrContentRecursive(course, process.getLevelId(), selectedText);

                    if (selectedObject instanceof Chapter selectedChapter) {
                        JOptionPane.showMessageDialog(contentDialog,
                                "Chapter: " + selectedChapter.getTitle(),
                                "Chapter Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else if (selectedObject instanceof CourseContent selectedContent) {
                        JOptionPane.showMessageDialog(contentDialog,
                                "Content: " + selectedContent.getContent() + "\nType: " + selectedContent.getClass().getSimpleName(),
                                "Content Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });


        // Add tree to a scroll pane
        JScrollPane scrollPane = new JScrollPane(chapterTree);
        contentDialog.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> contentDialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);
        contentDialog.add(bottomPanel, BorderLayout.SOUTH);

        contentDialog.setVisible(true);
    }

    private void addChapterContentRecursive(ChapterComponent chapterComponent, DefaultMutableTreeNode parentNode) {
        if (chapterComponent instanceof Chapter chapter) {
            // Add subchapters first
            for (ChapterComponent subChapter : chapter.getSubChapters()) {
                DefaultMutableTreeNode subChapterNode = new DefaultMutableTreeNode(subChapter.getTitle());
                parentNode.add(subChapterNode);
                addChapterContentRecursive(subChapter, subChapterNode); // Recursive call
            }

            // Then, add contents of the current chapter
            for (CourseContent content : chapter.getContents()) {
                DefaultMutableTreeNode contentNode = new DefaultMutableTreeNode(content.getContent());
                parentNode.add(contentNode);
            }
        }
    }

    private Object findChapterOrContentRecursive(Course course, int levelId, String title) {
        Level selectedLevel = course.getLevels().get(levelId - 1);
        if (selectedLevel == null) return null;

        for (Chapter chapter : selectedLevel.getChapters()) {
            Object result = findComponentInChapter(chapter, title);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private Object findComponentInChapter(ChapterComponent component, String title) {
        // Check if the selected node matches a chapter or sub-chapter
        if (component.getTitle().equals(title)) {
            return component;
        }

        if (component instanceof Chapter chapter) {
            // Search through all course contents in this chapter
            for (CourseContent content : chapter.getContents()) {
                if (content.getContent().equals(title)) {
                    return content;
                }
            }

            // Recursively check sub-chapters
            for (ChapterComponent subChapter : chapter.getSubChapters()) {
                Object found = findComponentInChapter(subChapter, title);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

}
