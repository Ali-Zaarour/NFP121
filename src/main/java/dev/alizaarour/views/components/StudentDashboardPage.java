package dev.alizaarour.views.components;

import dev.alizaarour.services.CourseProcessService;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.PaymentService;
import dev.alizaarour.services.UserService;
import dev.alizaarour.services.pack.VisaPaymentStrategy;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentDashboardPage implements Page, Observer {

    private JPanel totalCoursesBox, currentCoursesBox, doneCoursesBox;
    private JPanel summaryPanel;

    @Override
    public JPanel getPagePanel() {

        PaymentService.getInstance().addObserver(this);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        totalCoursesBox = createSummaryBox("Total Courses", "" + CourseService.getInstance().getTotalCourses());
        currentCoursesBox = createSummaryBox("Enrolled / In Progress Courses", "" + CourseProcessService.getInstance().getTotalEnrolledCourses());
        doneCoursesBox = createSummaryBox("Completed Courses", "" + CourseProcessService.getInstance().getTotalDoneCourses());

        summaryPanel.add(totalCoursesBox);
        summaryPanel.add(currentCoursesBox);
        summaryPanel.add(doneCoursesBox);

        mainPanel.add(summaryPanel, BorderLayout.NORTH);


        String[] columnNames = {"Num", "Title", "Created By", "Levels", "Enrolled", "Action"};
        DefaultTableModel model = new DefaultTableModel(CourseService.getInstance().getCourses(), columnNames) {
            // Only the Action column (index 5) is editable to handle button clicks
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        JTable table = new JTable(model);


        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }


    private JPanel createSummaryBox(String title, String value) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(150, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(Box.createVerticalStrut(10));
        box.add(titleLabel);
        box.add(Box.createVerticalGlue());
        box.add(valueLabel);
        box.add(Box.createVerticalStrut(10));

        return box;
    }

    @Override
    public void update() {
        // Assuming summaryPanel is an instance variable
        summaryPanel.removeAll();

        totalCoursesBox = createSummaryBox("Total Courses", "" + CourseService.getInstance().getTotalCourses());
        currentCoursesBox = createSummaryBox("Enrolled / In Progress Courses", "" + CourseProcessService.getInstance().getTotalEnrolledCourses());
        doneCoursesBox = createSummaryBox("Completed Courses", "" + CourseProcessService.getInstance().getTotalDoneCourses());

        summaryPanel.add(totalCoursesBox);
        summaryPanel.add(currentCoursesBox);
        summaryPanel.add(doneCoursesBox);

        summaryPanel.revalidate();
        summaryPanel.repaint();
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            Object firstColumnValue = table.getValueAt(row, 0);
            this.selectedRow = Integer.parseInt(firstColumnValue.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                SwingUtilities.invokeLater(() -> openEnrollmentDialog(selectedRow));
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        private void openEnrollmentDialog(int courseId) {
            // Retrieve the course based on the passed courseId.
            var currentCourse = CourseService.getInstance().getCourseById(courseId);
            if (currentCourse.getLevels().isEmpty() || currentCourse.getLevels() == null) {
                JOptionPane.showMessageDialog(null, "No levels available for this course.");
                return;
            }

            String courseName = currentCourse.getTitle();
            int numLevels = currentCourse.getLevels().size();

            // Create a modal dialog with fixed dimensions.
            JDialog dialog = new JDialog((Frame) null, "Enroll in " + courseName, true);
            dialog.setSize(500, 500);
            dialog.setLayout(new BorderLayout(10, 10));

            // Top panel: Course title label.
            JLabel titleLabel = new JLabel(courseName, SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            dialog.add(titleLabel, BorderLayout.NORTH);

            // Center panel: Card panel that shows the current level's details.
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            dialog.add(cardPanel, BorderLayout.CENTER);

            // Use an AtomicInteger to track the current level index (0-indexed).
            AtomicInteger currentIndex = new AtomicInteger(0);

            // A runnable to update the card panel with the current level's info.
            Runnable updateCard = () -> {
                cardPanel.removeAll();
                int index = currentIndex.get();
                var currentLevel = currentCourse.getLevels().get(index);
                String levelNum = "Level " + (index + 1);
                String levelTitle = currentLevel.getTitle();
                String levelFees = currentLevel.getFees() + " USD";
                String levelChapters = currentLevel.getChapters().size() + " Chapters";

                // Level number label.
                JLabel levelNumLabel = new JLabel(levelNum, SwingConstants.CENTER);
                levelNumLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                levelNumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Wrap the level title using HTML (with fixed width for wrapping).
                JLabel levelTitleLabel = new JLabel(levelTitle, SwingConstants.CENTER);
                levelTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                levelTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Fees and chapters labels.
                JLabel feesLabel = new JLabel(levelFees, SwingConstants.CENTER);
                feesLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                feesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel chaptersLabel = new JLabel(levelChapters, SwingConstants.CENTER);
                chaptersLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                chaptersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Enroll button.
                JButton enrollButton = new JButton("Enroll");
                enrollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                enrollButton.addActionListener(e -> openPaymentDialog(dialog, courseId, currentLevel.getNum(), currentLevel.getFees()));
                if (UserService.getInstance().checkIfAlreadyEnrolled(courseId, currentLevel.getNum())) {
                    var c = CourseProcessService.getInstance().getTheIdForTheNextLevel(courseId);
                    enrollButton.setEnabled(currentLevel.getNum() == c + 1);
                } else {
                    enrollButton.setEnabled(false);
                    enrollButton.setText("Already purchased");
                }


                // Add components to the card panel with spacing.
                cardPanel.add(Box.createVerticalStrut(20));
                cardPanel.add(levelNumLabel);
                cardPanel.add(Box.createVerticalStrut(10));
                cardPanel.add(levelTitleLabel);
                cardPanel.add(Box.createVerticalStrut(10));
                cardPanel.add(feesLabel);
                cardPanel.add(Box.createVerticalStrut(10));
                cardPanel.add(chaptersLabel);
                cardPanel.add(Box.createVerticalGlue());
                cardPanel.add(enrollButton);
                cardPanel.add(Box.createVerticalStrut(20));

                cardPanel.revalidate();
                cardPanel.repaint();
            };

            // Initialize the card panel.
            updateCard.run();

            JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton lastButton = new JButton("Last");
            JButton nextButton = new JButton("Next");

            lastButton.addActionListener(e -> {
                if (currentIndex.get() > 0) {
                    currentIndex.decrementAndGet();
                    updateCard.run();
                }
            });

            nextButton.addActionListener(e -> {
                if (currentIndex.get() < numLevels - 1) {
                    currentIndex.incrementAndGet();
                    updateCard.run();
                }
            });

            navPanel.add(lastButton);
            navPanel.add(nextButton);
            dialog.add(navPanel, BorderLayout.SOUTH);

            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        private void openPaymentDialog(JDialog parent, int courseId, int levelId, double fees) {
            parent.dispose();
            // Create a new modal dialog for payment options.
            JDialog paymentDialog = new JDialog(parent, "Select Payment Method", true);
            paymentDialog.setSize(400, 350);
            paymentDialog.setLayout(new BorderLayout(10, 10));

            // Create a tabbed pane with two tabs: Visa and OMT.
            JTabbedPane tabbedPane = new JTabbedPane();

            // --- Visa Panel ---
            JPanel visaPanel = new JPanel();
            visaPanel.setLayout(new BoxLayout(visaPanel, BoxLayout.Y_AXIS));
            visaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Visa header
            JLabel visaHeader = new JLabel("Enter Visa payment details:");
            visaHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(visaHeader);
            visaPanel.add(Box.createVerticalStrut(5));

            // Card Number
            JLabel cardNumberLabel = new JLabel("Card Number:");
            cardNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(cardNumberLabel);
            JTextField cardNumberField = new JTextField(20);
            cardNumberField.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardNumberField.getPreferredSize().height));
            cardNumberField.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(cardNumberField);
            visaPanel.add(Box.createVerticalStrut(5));

            // Expiry Date
            JLabel expiryLabel = new JLabel("Expiry Date:");
            expiryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(expiryLabel);
            JTextField expiryField = new JTextField(10);
            expiryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, expiryField.getPreferredSize().height));
            expiryField.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(expiryField);
            visaPanel.add(Box.createVerticalStrut(5));

            // CVV
            JLabel cvvLabel = new JLabel("CVV:");
            cvvLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(cvvLabel);
            JTextField cvvField = new JTextField(5);
            cvvField.setMaximumSize(new Dimension(Integer.MAX_VALUE, cvvField.getPreferredSize().height));
            cvvField.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaPanel.add(cvvField);
            visaPanel.add(Box.createVerticalStrut(10));

            // OK Button taking full width
            JButton visaOkButton = new JButton("OK");
            visaOkButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, visaOkButton.getPreferredSize().height));
            visaOkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            visaOkButton.addActionListener(e -> {
                // Validate all fields are filled.
                if (cardNumberField.getText().trim().isEmpty() ||
                        expiryField.getText().trim().isEmpty() ||
                        cvvField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(paymentDialog, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    var newCourseId = CourseProcessService.getInstance().createCourseProcess(courseId, levelId);
                    if (newCourseId == -1) {
                        JOptionPane.showMessageDialog(null, "Failed to create course process.");
                        return;
                    }
                    PaymentService.getInstance().processPayment(newCourseId, fees, new VisaPaymentStrategy());
                    JOptionPane.showMessageDialog(paymentDialog, "Visa Payment processed!");
                    paymentDialog.dispose();
                }
            });
            visaPanel.add(visaOkButton);

            // --- OMT Panel ---
            JPanel omtPanel = new JPanel();
            omtPanel.setLayout(new BoxLayout(omtPanel, BoxLayout.Y_AXIS));
            omtPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // OMT header
            JLabel omtHeader = new JLabel("Enter OMT payment details:");
            omtHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtHeader);
            omtPanel.add(Box.createVerticalStrut(5));

            // OMT Card Number
            JLabel omtCardLabel = new JLabel("OMT Card Number:");
            omtCardLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtCardLabel);
            JTextField omtCardField = new JTextField(20);
            omtCardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, omtCardField.getPreferredSize().height));
            omtCardField.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtCardField);
            omtPanel.add(Box.createVerticalStrut(5));

            // Name
            JLabel omtNameLabel = new JLabel("Name:");
            omtNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtNameLabel);
            JTextField omtNameField = new JTextField(20);
            omtNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, omtNameField.getPreferredSize().height));
            omtNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtNameField);
            omtPanel.add(Box.createVerticalStrut(5));

            // Password
            JLabel omtPasswordLabel = new JLabel("Password:");
            omtPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtPasswordLabel);
            JPasswordField omtPasswordField = new JPasswordField(20);
            omtPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, omtPasswordField.getPreferredSize().height));
            omtPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtPanel.add(omtPasswordField);
            omtPanel.add(Box.createVerticalStrut(10));

            // OK Button taking full width for OMT
            JButton omtOkButton = new JButton("OK");
            omtOkButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, omtOkButton.getPreferredSize().height));
            omtOkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            omtOkButton.addActionListener(e -> {
                if (omtCardField.getText().trim().isEmpty() ||
                        omtNameField.getText().trim().isEmpty() ||
                        String.valueOf(omtPasswordField.getPassword()).trim().isEmpty()) {
                    JOptionPane.showMessageDialog(paymentDialog, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    var newCourseId = CourseProcessService.getInstance().createCourseProcess(courseId, levelId);
                    if (newCourseId == -1) {
                        JOptionPane.showMessageDialog(null, "Failed to create course process.");
                        return;
                    }
                    PaymentService.getInstance().processPayment(newCourseId, fees, new VisaPaymentStrategy());
                    JOptionPane.showMessageDialog(paymentDialog, "OMT Payment processed!");
                    paymentDialog.dispose();
                }
            });
            omtPanel.add(omtOkButton);

            // Add both panels as tabs.
            tabbedPane.addTab("Visa", visaPanel);
            tabbedPane.addTab("OMT", omtPanel);

            paymentDialog.add(tabbedPane, BorderLayout.CENTER);

            // Optional bottom panel with a Close button.
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> paymentDialog.dispose());
            bottomPanel.add(closeButton);
            paymentDialog.add(bottomPanel, BorderLayout.SOUTH);

            paymentDialog.setLocationRelativeTo(parent);
            paymentDialog.setVisible(true);
        }

    }
}
