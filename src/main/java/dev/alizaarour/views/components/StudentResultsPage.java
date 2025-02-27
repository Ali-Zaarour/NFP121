package dev.alizaarour.views.components;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.Student;
import dev.alizaarour.models.User;
import dev.alizaarour.services.CourseProcessService;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.UserService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class StudentResultsPage implements Page, Observer {

    private JPanel mainPanel;
    private DefaultTableModel tableModel;

    public StudentResultsPage() {
        CourseProcessService.getInstance().addObserver(this);
        init();
    }

    private void init() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Title
        JLabel titleLabel = new JLabel("My Note");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"ID", "Course Name", "Level", "Note", "Date"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };

        JTable resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadResults();
    }

    @Override
    public JPanel getPagePanel() {
        return mainPanel;
    }

    @Override
    public void update() {
        loadResults(); // Reload data when notified
    }

    private void loadResults() {
        User activeUser = UserService.getInstance().getActiveUser();
        if (!(activeUser instanceof Student student)) {
            return;
        }

        List<CourseProcess> results = student.getCourseProcesses().stream()
                .filter(cp -> cp.getQuizNote() > 0)
                .sorted(Comparator.comparing(CourseProcess::getQuizDate).reversed())
                .toList();

        tableModel.setRowCount(0); // Clear existing data

        for (CourseProcess process : results) {
            Object[] rowData = {
                    process.getProcessId(),
                    CourseService.getInstance().getCourseById(process.getCourseId()).getTitle(),
                    "Level " + process.getLevelId() + " - " + CourseService.getInstance().getCourseById(process.getCourseId()).getLevels().get(process.getLevelId() - 1).getTitle(),
                    process.getQuizNote(),
                    process.getQuizDate()
            };

            tableModel.addRow(rowData);
        }
    }
}
