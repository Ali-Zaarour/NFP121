package dev.alizaarour.views.components;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.models.Course;
import dev.alizaarour.models.Level;
import dev.alizaarour.models.Meeting;
import dev.alizaarour.services.CourseService;
import dev.alizaarour.services.MeetingService;
import dev.alizaarour.utils.Observer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MeetingDialog extends JDialog {
    private final JDatePickerImpl datePicker;
    private final JTextField timeField;
    private final JTextField durationField;
    private final JComboBox<Course> courseComboBox;
    private final JComboBox<Level> levelComboBox;
    private final Meeting meeting;
    private final Observer observer;

    public MeetingDialog(Meeting meeting, Observer observer) {
        this.meeting = meeting;
        this.observer = observer;

        setTitle(meeting == null ? "Create Meeting" : "Update Meeting");
        setSize(550, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;

        int row = 0;

        // Date Picker
        addLabel("Date:", gbc, row, 0);
        datePicker = createDatePicker();
        addComponent(datePicker, gbc, row++, 1);

        // Time
        addLabel("Time (HH:MM):", gbc, row, 0);
        timeField = new JTextField(meeting != null ? meeting.getTime().toString() : "");
        addComponent(timeField, gbc, row++, 1);

        // Duration
        addLabel("Duration (min):", gbc, row, 0);
        durationField = new JTextField(meeting != null ? String.valueOf(meeting.getDuration()) : "");
        addComponent(durationField, gbc, row++, 1);

        // Teacher Name (Disabled)
        addLabel("Teacher:", gbc, row, 0);
        JTextField teacherField = new JTextField(SessionManager.getInstance().getUser().getName());
        teacherField.setEnabled(false);
        addComponent(teacherField, gbc, row++, 1);

        // Course Selection
        addLabel("Course:", gbc, row, 0);
        courseComboBox = new JComboBox<>(CourseService.getInstance().getCoursesForCurrentTeacher().toArray(new Course[0]));
        courseComboBox.insertItemAt(null, 0); // Add empty selection at the start
        courseComboBox.setSelectedIndex(0);
        addComponent(courseComboBox, gbc, row++, 1);

        // Level Selection (Dynamic)
        addLabel("Course Level:", gbc, row, 0);
        levelComboBox = new JComboBox<>();
        addComponent(levelComboBox, gbc, row++, 1);

        // Load levels dynamically
        courseComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateLevels();
            }
        });

        // Save Button
        JButton saveButton = new JButton(meeting == null ? "Create" : "Update");
        saveButton.addActionListener(e -> saveMeeting());
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        // Preload data if updating
        if (meeting != null) {
            courseComboBox.setSelectedItem(getCourseById(meeting.getCourseId()));
            updateLevels();
            levelComboBox.setSelectedItem(getLevelById(meeting.getCourseLevelId()));
        }

        setVisible(true);
    }

    private void addLabel(String text, GridBagConstraints gbc, int row, int col) {
        gbc.gridy = row;
        gbc.gridx = col;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setPreferredSize(new Dimension(120, 30));
        add(label, gbc);
    }

    private void addComponent(JComponent component, GridBagConstraints gbc, int row, int col) {
        gbc.gridy = row;
        gbc.gridx = col;
        component.setPreferredSize(new Dimension(250, 30));
        add(component, gbc);
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        datePicker.setPreferredSize(new Dimension(250, 30));

        model.addChangeListener(e -> {
            Date selectedDate = model.getValue();
            if (selectedDate != null) {
                datePicker.getJFormattedTextField().setText(new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
            }
        });

        return datePicker;
    }


    static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String pattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().isEmpty()) return null;
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value instanceof Date) {
                return dateFormatter.format((Date) value);
            }
            return "";
        }
    }

    private void updateLevels() {
        levelComboBox.removeAllItems();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        if (selectedCourse != null) {
            for (Level level : selectedCourse.getLevels()) {
                levelComboBox.addItem(level);
            }
        }
    }

    private Course getCourseById(int courseId) {
        return CourseService.getInstance().getCoursesForCurrentTeacher().stream()
                .filter(course -> course.getPk() == courseId)
                .findFirst()
                .orElse(null);
    }

    private Level getLevelById(int levelId) {
        for (Course course : CourseService.getInstance().getCoursesForCurrentTeacher()) {
            for (Level level : course.getLevels()) {
                if (level.getNum() == levelId) {
                    return level;
                }
            }
        }
        return null;
    }

    private void saveMeeting() {
        try {
            LocalDate date = getSelectedDateFromPicker(datePicker);
            LocalTime time = LocalTime.parse(timeField.getText());
            int duration = Integer.parseInt(durationField.getText());
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            Level selectedLevel = (Level) levelComboBox.getSelectedItem();

            if (selectedCourse == null || selectedLevel == null) {
                throw new Exception("Invalid Course or Level selection");
            }

            int teacherId = SessionManager.getInstance().getUser().getUserId();
            boolean result;

            if (meeting == null) {
                result = MeetingService.getInstance().addMeeting(
                        new Meeting(date, time, duration, teacherId, selectedCourse.getPk(),
                                selectedLevel.getNum(), List.of()));
            } else {
                meeting.setDate(date);
                meeting.setTime(time);
                meeting.setDuration(duration);
                meeting.setTeacherId(teacherId);
                meeting.setCourseId(selectedCourse.getPk());
                meeting.setCourseLevelId(selectedLevel.getNum());

                result = MeetingService.getInstance().updateMeeting(meeting.getMeetingId(), meeting);
            }

            if (!result) {
                JOptionPane.showMessageDialog(this,
                        "Conflict detected: A meeting for this teacher already exists at this time.",
                        "Meeting Conflict", JOptionPane.WARNING_MESSAGE);
                return;
            }

            observer.update();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private LocalDate getSelectedDateFromPicker(JDatePickerImpl datePicker) {
        UtilDateModel model = (UtilDateModel) datePicker.getModel();
        if (!model.isSelected()) {
            return null;
        }
        return model.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
