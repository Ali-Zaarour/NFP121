package dev.alizaarour.views.components;

import dev.alizaarour.models.Course;
import dev.alizaarour.models.Level;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class TeacherManageCoursesPage implements Page, Observer {
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private List<Course> teacherCourses;

    public TeacherManageCoursesPage() {
        CourseService.getInstance().addObserver(this);
        loadTeacherCourses(); // Load courses when initializing
    }

    @Override
    public JPanel getPagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(1000, 600));

        JLabel label = new JLabel("Manage Your Courses", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.NORTH);

        // Initialize Table
        initializeTable();

        // Scroll Pane to contain the table
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void initializeTable() {
        // Table Columns
        String[] columns = {"ID", "Title", "Number of Levels", "Total Fees", "Update", "Delete"};

        // Table Model (Non-editable)
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Only "Update" and "Delete" buttons are clickable
            }
        };

        // Load courses into the table
        loadTableData();

        // Create JTable
        coursesTable = new JTable(tableModel);
        coursesTable.setRowHeight(30);

        // Add row striping effect (alternate row colors)
        coursesTable.setDefaultRenderer(Object.class, new RowStripeRenderer());

        // Set "Update" and "Delete" as buttons with hover effect
        coursesTable.getColumn("Update").setCellRenderer(new ButtonRenderer("Update"));
        coursesTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", coursesTable));

        coursesTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        coursesTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", coursesTable));
    }

    private void loadTeacherCourses() {
        teacherCourses = CourseService.getInstance().getCoursesForCurrentTeacher();
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // Clear previous data

        for (Course course : teacherCourses) {
            int numLevels = course.getLevels().size();
            double totalFees = course.getLevels().stream().mapToDouble(Level::getFees).sum();

            tableModel.addRow(new Object[]{
                    course.getPk(),
                    course.getTitle(),
                    numLevels,
                    totalFees,
                    "Update",
                    "Delete"
            });
        }
    }

    @Override
    public void update() {
        loadTeacherCourses(); // Reload data when notified
        loadTableData(); // Refresh table data
    }

    /**
     * Renderer for JButton inside JTable with hover effect
     */
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        private final String action;

        public ButtonRenderer(String action) {
            this.action = action;
            setOpaque(true);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());

            // Set hover effect colors
            if ("Delete".equals(action)) {
                setBackground(new Color(220, 53, 69)); // Red
            } else {
                setBackground(new Color(0, 123, 255)); // Blue
            }

            return this;
        }
    }

    /**
     * Editor for JButton inside JTable with hover effect
     */
    private static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final JTable table;
        private final String action;

        public ButtonEditor(JCheckBox checkBox, String action, JTable table) {
            super(checkBox);
            this.action = action;
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> {
                fireEditingStopped();
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int courseId = (int) table.getValueAt(row, 0);
                    if (action.equals("Delete")) {
                        deleteCourse(courseId);
                    } else if (action.equals("Update")) {
                        updateCourse(courseId);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());

            // Set hover effect colors
            if ("Delete".equals(action)) {
                button.setBackground(new Color(220, 53, 69)); // Red
            } else {
                button.setBackground(new Color(0, 123, 255)); // Blue
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        private void deleteCourse(int courseId) {
            // Ask user for confirmation
            int confirmation = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this course?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirmation == JOptionPane.YES_OPTION) {
                CourseService.getInstance().removeCourse(courseId);
                JOptionPane.showMessageDialog(null, "Course deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void updateCourse(int courseId) {
            new TeacherEditCoursePage(courseId);
        }
    }

    /**
     * Custom row renderer for alternating row colors (striped effect)
     */
    private static class RowStripeRenderer extends DefaultTableCellRenderer {
        private static final Color EVEN_ROW_COLOR = new Color(240, 240, 240); // Light Gray
        private static final Color ODD_ROW_COLOR = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                component.setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
            }

            return component;
        }
    }
}
