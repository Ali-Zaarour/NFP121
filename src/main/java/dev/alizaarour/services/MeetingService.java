package dev.alizaarour.services;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.Meeting;
import dev.alizaarour.utils.Observable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class MeetingService extends Observable {

    private static final MeetingService instance = new MeetingService();

    private MeetingService() {
    }

    public static synchronized MeetingService getInstance() {
        return instance;
    }

    public boolean addMeeting(Meeting newMeeting) {
        List<Meeting> existingMeetings = ApplicationInitializer.dataSchema.getMeetings();

        int teacherId = newMeeting.getTeacherId();
        LocalDate date = newMeeting.getDate();
        LocalTime startTime = newMeeting.getTime();
        LocalTime endTime = startTime.plusMinutes(newMeeting.getDuration());

        boolean hasConflict = existingMeetings.stream()
                .filter(m -> m.getTeacherId() == teacherId && m.getDate().equals(date)) // Same teacher, same date
                .anyMatch(m -> {
                    LocalTime existingStart = m.getTime();
                    LocalTime existingEnd = existingStart.plusMinutes(m.getDuration());

                    return (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart));
                });

        if (hasConflict) {
            return false;
        }

        existingMeetings.add(newMeeting);
        notifyObservers();
        return true;
    }


    public void removeMeeting(int meetingID) {
        ApplicationInitializer.dataSchema.getMeetings().removeIf(meeting -> meeting.getMeetingId() == meetingID);
        notifyObservers();
    }

    public List<Meeting> getMeetingsForCurrentTeacher() {
        int teacherId = SessionManager.getInstance().getUser().getUserId();
        return ApplicationInitializer.dataSchema.getMeetings().stream()
                .filter(meeting -> meeting.getTeacherId() == teacherId)
                .sorted(Comparator.comparing(Meeting::getDate)
                        .thenComparing(Meeting::getTime)
                        .reversed())
                .toList();
    }

    public Meeting getMeetingById(int meetingID) {
        Meeting meeting;
        for (int i = 0; i < ApplicationInitializer.dataSchema.getMeetings().size(); i++) {
            meeting = ApplicationInitializer.dataSchema.getMeetings().get(i);
            if (meeting.getMeetingId() == meetingID) {
                return meeting;
            }
        }
        return null;
    }

    public boolean updateMeeting(int meetingID, Meeting newMeeting) {
        List<Meeting> meetings = ApplicationInitializer.dataSchema.getMeetings();

        int teacherId = newMeeting.getTeacherId();
        LocalDate date = newMeeting.getDate();
        LocalTime startTime = newMeeting.getTime();
        LocalTime endTime = startTime.plusMinutes(newMeeting.getDuration());

        boolean hasConflict = meetings.stream()
                .filter(m -> m.getMeetingId() != meetingID) // Ignore the meeting being updated
                .filter(m -> m.getTeacherId() == teacherId && m.getDate().equals(date)) // Same teacher, same date
                .anyMatch(m -> {
                    LocalTime existingStart = m.getTime();
                    LocalTime existingEnd = existingStart.plusMinutes(m.getDuration());

                    return (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart));
                });

        if (hasConflict) {
            return false;
        }

        meetings.removeIf(m -> m.getMeetingId() == meetingID);
        meetings.add(newMeeting);
        notifyObservers();
        return true;
    }


    public void markDoneMeetings() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        for (Meeting meeting : getMeetingsForCurrentTeacher()) {
            LocalTime endTime = meeting.getTime().plusMinutes(meeting.getDuration());
            meeting.setDone(meeting.getDate().isBefore(today) || (meeting.getDate().equals(today) && now.isAfter(endTime)));
        }
    }


}
