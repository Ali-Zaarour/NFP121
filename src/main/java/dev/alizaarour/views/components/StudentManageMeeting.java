package dev.alizaarour.views.components;

import dev.alizaarour.models.Meeting;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.MeetingService;
import dev.alizaarour.services.UserService;
import dev.alizaarour.utils.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentManageMeeting implements Page, Observer {
    private final JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private DefaultListModel<String> activeMeetingsListModel;
    private JList<String> activeMeetingsList;


    public StudentManageMeeting() {
        MeetingService.getInstance().addObserver(this);
        MeetingService.getInstance().markDoneMeetingsForStudent();
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

        JButton joinMeetingButton = new JButton("Join Meeting");
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

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        mainPanel.add(searchPanel, gbc);
    }

    private void createMeetingTable(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.9;

        String[] columnNames = {"ID", "Date", "Time", "Duration", "Teacher", "Course", "Level", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8;
            }
        };

        JTable meetingTable = new JTable(tableModel);
        meetingTable.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(meetingTable);
        scrollPane.setPreferredSize(new Dimension(1000, 800));

        mainPanel.add(scrollPane, gbc);
        loadMeetings();
    }

    private void loadMeetings() {
        MeetingService.getInstance().markDoneMeetingsForStudent();
        tableModel.setRowCount(0);
        java.util.List<Meeting> meetings = MeetingService.getInstance().getMeetingForCurrentStudent();
        updateMeetingTable(meetings);
    }

    private void filterMeetings() {
        MeetingService.getInstance().markDoneMeetingsForStudent();

        String searchText = searchField.getText().trim().toLowerCase();
        java.util.List<Meeting> filteredMeetings = MeetingService.getInstance().getMeetingForCurrentStudent().stream()
                .filter(m -> (m.getMeetingId() + " " + m.getDate() + " " + m.getTime() + " " + m.getDuration()
                        + " " + m.getTeacherId() + " " + m.getCourseId() + " " + m.getCourseLevelId() + " " + m.getDone())
                        .toLowerCase().contains(searchText))
                .toList();

        tableModel.setRowCount(0);
        updateMeetingTable(filteredMeetings);
    }

    private void updateMeetingTable(java.util.List<Meeting> meetings) {
        for (Meeting meeting : meetings) {
            tableModel.addRow(new Object[]{
                    meeting.getMeetingId(), meeting.getDate(), meeting.getTime(),
                    meeting.getDuration(),
                    UserService.getInstance().findUserWithCondition(c -> c.getUserId() == meeting.getTeacherId()).get().getName(),
                    CourseService.getInstance().getCourseById(meeting.getCourseId()).getTitle(),
                    meeting.getCourseLevelId(),
                    meeting.getDone() ? "Done" : "Active"
            });
        }
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

    private void loadActiveMeetings() {
        activeMeetingsListModel.clear();
        for (Meeting meeting : MeetingService.getInstance().getActiveMeetingsForStudent()) {
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
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a meeting to join.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extract meeting ID from selected text
        String selectedMeetingText = activeMeetingsListModel.getElementAt(selectedIndex);
        String meetingIdText = selectedMeetingText.split(" ")[1]; // Extracts meeting ID
        int meetingId = Integer.parseInt(meetingIdText);

        Meeting meeting = MeetingService.getInstance().getMeetingById(meetingId);
        if (meeting == null) {
            JOptionPane.showMessageDialog(mainPanel, "Meeting not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        meeting.addStudent(UserService.getInstance().getActiveUser().getUserId());

        // ✅ Open the modal Meeting UI Dialog
        new MeetingFrame((JFrame) SwingUtilities.getWindowAncestor(mainPanel), meeting);
    }

    private void startAutoRefresh() {
        Timer timer = new Timer(60000, e -> SwingUtilities.invokeLater(() -> {
            MeetingService.getInstance().markDoneMeetingsForStudent();
            loadActiveMeetings();
            loadMeetings();
        }));

        timer.setRepeats(true);
        timer.start();
    }
}
