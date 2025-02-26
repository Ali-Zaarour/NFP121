package dev.alizaarour.views.components;

import dev.alizaarour.models.CourseProcess;
import dev.alizaarour.models.Student;
import dev.alizaarour.models.User;
import dev.alizaarour.services.CourseProcessService;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.PaymentService;
import dev.alizaarour.services.UserService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class StudentPaymentHistory implements Page, Observer {

    private JPanel panel;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;

    public StudentPaymentHistory() {
        PaymentService.getInstance().addObserver(this);
        init();
    }

    private void init() {
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Label (Left-Aligned)
        JLabel titleLabel = new JLabel("Student Payment History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {
                "ID", "Name", "Level", "State", "Fees", "Paid", "Type", "Date"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };

        paymentTable = new JTable(tableModel);
        paymentTable.setFillsViewportHeight(true);
        paymentTable.setRowHeight(25);

        // Center-align text in all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < paymentTable.getColumnCount(); i++) {
            paymentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Auto-resize column widths dynamically
        paymentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumnWidths(paymentTable);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Total Payment Row
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalAmountLabel = new JLabel("Total Paid: $0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalAmountLabel);

        panel.add(totalPanel, BorderLayout.SOUTH);

        loadPaymentHistory();
    }

    @Override
    public JPanel getPagePanel() {
        return panel;
    }

    @Override
    public void update() {
        loadPaymentHistory(); // Reload table when notified
    }

    private void loadPaymentHistory() {
        User activeUser = UserService.getInstance().getActiveUser();
        if (!(activeUser instanceof Student student)) {
            return;
        }

        List<CourseProcess> payments = student.getCourseProcesses();
        tableModel.setRowCount(0);

        for (CourseProcess process : payments) {
            // Prepare row data
            Object[] rowData = {
                    process.getProcessId(),
                    CourseService.getInstance().getCourseById(process.getCourseId()).getTitle(),
                    "Level " + process.getLevelId() + " - "
                            + CourseService.getInstance().getCourseById(process.getCourseId()).getLevels().get(process.getLevelId() - 1).getTitle(),
                    process.getState().getClass().getSimpleName(),
                    "$" + process.getPaymentFees(),
                    process.isPaid() ? "✔" : "✘",
                    process.getPaymentType(),
                    process.isPaid() ? process.getPaymentDate() : "-"
            };

            tableModel.addRow(rowData);
        }

        // Update total amount label
        totalAmountLabel.setText("Total Paid: $" + String.format("%.2f", CourseProcessService.getInstance().getTotalFeesPaid()));

        // Resize column widths after data is loaded
        resizeColumnWidths(paymentTable);
    }

    /**
     * Adjusts column widths dynamically based on content.
     */
    private void resizeColumnWidths(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Minimum width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}
