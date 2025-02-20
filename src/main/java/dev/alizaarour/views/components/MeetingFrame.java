package dev.alizaarour.views.components;

import dev.alizaarour.models.Meeting;
import dev.alizaarour.services.CourseService;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MeetingFrame extends JDialog {
    private final JLabel timerLabel;

    public MeetingFrame(JFrame parent, Meeting meeting) {
        super(parent, "Meeting "
                        + meeting.getMeetingId() + " - "
                        + meeting.getTime() + " (" + meeting.getDuration()
                        + " min) | " + "Course: "
                        + CourseService.getInstance().getCourseById(meeting.getCourseId()).getTitle() + " | "
                        + CourseService.getInstance().getCourseById(meeting.getCourseId()).getLevels().stream()
                        .filter(l -> l.getNum() == meeting.getCourseLevelId()).findFirst().get().getTitle()
                , true);
        setSize(1200, 800);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(1, 1));

        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        // ** Meeting Panel (85%) **
        JPanel meetingPanel = new JPanel(new BorderLayout());
        meetingPanel.setBackground(Color.WHITE);
        meetingPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Meeting", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // ** Timer Label for Live Clock **
        timerLabel = new JLabel(getCurrentTime());
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        startTimer();

        titlePanel.add(titleLabel);
        titlePanel.add(timerLabel);

        JLabel meetingImage = new JLabel();
        URL imageUrl = getClass().getResource("/images/loading.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            meetingImage.setIcon(new ImageIcon(image));
        }
        meetingImage.setHorizontalAlignment(JLabel.CENTER);

        meetingPanel.add(titlePanel, BorderLayout.NORTH);
        meetingPanel.add(meetingImage, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.95;
        mainContainer.add(meetingPanel, gbc);

        // ** Modern Participants Panel (15%) **
        JPanel participantsPanel = new JPanel(new BorderLayout());
        participantsPanel.setBackground(Color.WHITE);
        participantsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel participantsLabel = new JLabel("Participants", SwingConstants.CENTER);
        participantsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        participantsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultListModel<String> participantListModel = new DefaultListModel<>();
        List<String> randomNames = List.of(
                "****************","****************","****************","****************","****************","****************",
                "****************","****************","****************","****************","****************","****************",
                "****************","****************"
        );

        for (String name : randomNames) {
            participantListModel.addElement(name);
        }

        JList<String> participantsList = new JList<>(participantListModel);
        participantsList.setCellRenderer(new ModernParticipantRenderer());
        JScrollPane participantScrollPane = new JScrollPane(participantsList);
        participantScrollPane.setPreferredSize(new Dimension(220, 0));

        participantsPanel.add(participantsLabel, BorderLayout.NORTH);
        participantsPanel.add(participantScrollPane, BorderLayout.CENTER);

        gbc.gridx = 2;
        gbc.weightx = 0.05;
        mainContainer.add(participantsPanel, gbc);

        add(mainContainer, BorderLayout.CENTER);

        setVisible(true);
    }

    private static class ModernParticipantRenderer extends DefaultListCellRenderer {
        private final Random random = new Random();

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            panel.setBackground(isSelected ? new Color(220, 230, 255) : Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            panel.setPreferredSize(new Dimension(200, 50));

            // ** Generate a random color for the avatar **
            Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

            JLabel avatar = new JLabel(getInitials(value.toString()), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw the circular background
                    g2d.setColor(randomColor);
                    g2d.fillOval(0, 0, getWidth(), getHeight());

                    // Draw the initials in the center
                    super.paintComponent(g);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(35, 35);
                }
            };

            avatar.setFont(new Font("Arial", Font.BOLD, 14));
            avatar.setOpaque(false);
            avatar.setForeground(Color.WHITE);
            avatar.setHorizontalAlignment(JLabel.CENTER);

            // ** Name Label **
            JLabel nameLabel = new JLabel(value.toString());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            panel.add(avatar);
            panel.add(nameLabel);
            return panel;
        }

        private String getInitials(String name) {
            String[] words = name.split(" ");
            if (words.length >= 2) {
                return words[0].charAt(0) + "" + words[1].charAt(0);
            } else {
                return words[0].substring(0, Math.min(2, words[0].length())).toUpperCase();
            }
        }
    }


    private void startTimer() {
        Timer timer = new Timer(1000, e -> timerLabel.setText(getCurrentTime()));
        timer.start();
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}
