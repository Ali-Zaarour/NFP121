package dev.alizaarour.views.components;

import dev.alizaarour.models.Meeting;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.MeetingService;
import dev.alizaarour.services.UserService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TeacherManageMeeting implements Page, Observer {
    private final JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable meetingTable;
    private JTextField searchField;
    private DefaultListModel<String> activeMeetingsListModel;
    private JList<String> activeMeetingsList;
    private JButton joinMeetingButton;


    public TeacherManageMeeting() {
        MeetingService.getInstance().addObserver(this);
        MeetingService.getInstance().markDoneMeetings();
        mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0;

        createActiveMeetingsRow(gbc); // Active Meetings Row
        createSearchRow(gbc);
        createMeetingTable(gbc);

        startAutoRefresh();
    }

    /**
     * Creates a row to display currently active meetings.
     */
    private void createActiveMeetingsRow(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.01;

        JPanel activeMeetingsPanel = new JPanel(new BorderLayout(10, 10));

        JLabel activeMeetingsLabel = new JLabel("Active Meetings:");
        activeMeetingsLabel.setPreferredSize(new Dimension(120, 40));
        activeMeetingsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // ** JList to show active meetings **
        activeMeetingsListModel = new DefaultListModel<>();
        activeMeetingsList = new JList<>(activeMeetingsListModel);
        loadActiveMeetings();
        activeMeetingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeMeetingsList.setVisibleRowCount(1);
        activeMeetingsList.setFixedCellHeight(30);
        activeMeetingsList.setPreferredSize(new Dimension(400, 80)); // Adjusted height

        joinMeetingButton = new JButton("Join Meeting");
        joinMeetingButton.setPreferredSize(new Dimension(150, 40));
        joinMeetingButton.addActionListener(e -> openMeetingModal());

        activeMeetingsPanel.add(activeMeetingsLabel, BorderLayout.WEST);
        activeMeetingsPanel.add(activeMeetingsList, BorderLayout.CENTER);
        activeMeetingsPanel.add(joinMeetingButton, BorderLayout.EAST);

        mainPanel.add(activeMeetingsPanel, gbc);
    }

    private void createSearchRow(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.01;

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setPreferredSize(new Dimension(80, 40));
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 40));
        searchField.addCaretListener(e -> filterMeetings());

        JButton createMeetingButton = new JButton("Create Meeting");
        createMeetingButton.setPreferredSize(new Dimension(150, 40));
        createMeetingButton.addActionListener(e -> openMeetingDialog(null));

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(createMeetingButton, BorderLayout.EAST);

        mainPanel.add(searchPanel, gbc);
    }

    private void createMeetingTable(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.9;

        String[] columnNames = {"ID", "Date", "Time", "Duration", "Teacher", "Course", "Level","Status", "Update", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8;
            }
        };

        meetingTable = new JTable(tableModel);
        meetingTable.setRowHeight(35);
        meetingTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        meetingTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", meetingTable));
        meetingTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        meetingTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", meetingTable));

        JScrollPane scrollPane = new JScrollPane(meetingTable);
        scrollPane.setPreferredSize(new Dimension(1000, 800));

        mainPanel.add(scrollPane, gbc);
        loadMeetings();
    }

    private void loadMeetings() {
        MeetingService.getInstance().markDoneMeetings();
        tableModel.setRowCount(0);
        List<Meeting> meetings = MeetingService.getInstance().getMeetingsForCurrentTeacher();
        updateMeetingTable(meetings);
    }

    private void filterMeetings() {
        MeetingService.getInstance().markDoneMeetings();

        String searchText = searchField.getText().trim().toLowerCase();
        List<Meeting> filteredMeetings = MeetingService.getInstance().getMeetingsForCurrentTeacher().stream()
                .filter(m -> (m.getMeetingId() + " " + m.getDate() + " " + m.getTime() + " " + m.getDuration()
                        + " " + m.getTeacherId() + " " + m.getCourseId() + " " + m.getCourseLevelId() + " " + m.getDone())
                        .toLowerCase().contains(searchText))
                .toList();

        tableModel.setRowCount(0);
        updateMeetingTable(filteredMeetings);
    }

    private void updateMeetingTable(List<Meeting> meetings) {
        for (Meeting meeting : meetings) {
            tableModel.addRow(new Object[]{
                    meeting.getMeetingId(), meeting.getDate(), meeting.getTime(),
                    meeting.getDuration(),
                    UserService.getInstance().findUserWithCondition(c -> c.getUserId() == meeting.getTeacherId()).get().getName(),
                    CourseService.getInstance().getCourseById(meeting.getCourseId()).getTitle(),
                    meeting.getCourseLevelId(),
                    meeting.getDone() ? "Done" : "Active",
                    "Update", "Delete"
            });
        }
    }

    private void openMeetingDialog(Meeting meeting) {
        new MeetingDialog(meeting, this);
        loadActiveMeetings();
    }

    @Override
    public void update() {
        loadMeetings();
        loadActiveMeetings();
    }

    @Override
    public JPanel getPagePanel() {
        return mainPanel;
    }

    /**
     * Renderer for JButton inside JTable.
     */
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground("Delete".equals(value) ? new Color(220, 53, 69) : new Color(0, 123, 255));
            setForeground(Color.WHITE);
            return this;
        }
    }

    /**
     * Editor for JButton inside JTable.
     */
    private class ButtonEditor extends DefaultCellEditor {
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
                    int meetingId = (int) table.getValueAt(row, 0);
                    if (action.equals("Delete")) {
                        deleteMeeting(meetingId);
                    } else if (action.equals("Update")) {
                        openMeetingDialog(MeetingService.getInstance().getMeetingById(meetingId));
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            button.setBackground("Delete".equals(action) ? new Color(220, 53, 69) : new Color(0, 123, 255));
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        private void deleteMeeting(int meetingId) {
            int confirmation = JOptionPane.showConfirmDialog(
                    null, "Are you sure you want to delete this meeting?", "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
            );
            if (confirmation == JOptionPane.YES_OPTION) {
                MeetingService.getInstance().removeMeeting(meetingId);
            }
        }
    }

    private void loadActiveMeetings() {
        activeMeetingsListModel.clear();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Meeting> activeMeetings = MeetingService.getInstance().getMeetingsForCurrentTeacher().stream()
                .filter(m -> Boolean.FALSE.equals(m.getDone()))
                .filter(m -> m.getDate().equals(today))
                .filter(m -> {
                    LocalTime startTime = m.getTime();
                    LocalTime endTime = startTime.plusMinutes(m.getDuration());
                    return (now.equals(startTime) || now.isAfter(startTime)) &&
                            (now.equals(endTime) || now.isBefore(endTime));
                })
                .toList();

        for (Meeting meeting : activeMeetings) {
            String courseName = CourseService.getInstance().getCourseById(meeting.getCourseId()).getTitle();
            String level = "Level " + meeting.getCourseLevelId();
            int studentCount = meeting.getStudentIds().size();

            activeMeetingsListModel.addElement(
                    "Meeting " + meeting.getMeetingId() + " - "
                            + meeting.getTime() + " (" + meeting.getDuration() + " min) | "
                            + "Course: " + courseName + " | "
                            + level + " | "
                            + "Students: " + studentCount
            );
        }
    }


    private void openMeetingModal() {
        int selectedIndex = activeMeetingsList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedMeetingText = activeMeetingsListModel.getElementAt(selectedIndex);
            JOptionPane.showMessageDialog(mainPanel,
                    "Meeting Details: \n" + selectedMeetingText,
                    "Active Meeting", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Please select a meeting to join.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void startAutoRefresh() {
        Timer timer = new Timer(60000, e -> {
            SwingUtilities.invokeLater(() -> {
                MeetingService.getInstance().markDoneMeetings();
                loadActiveMeetings();
                loadMeetings();
            });
        });

        timer.setRepeats(true);
        timer.start();
    }

}
