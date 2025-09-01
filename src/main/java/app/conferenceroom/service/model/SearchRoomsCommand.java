package app.conferenceroom.service.model;

public record SearchRoomsCommand(
        TimeRange meetingTime,
        int numberOfAttendees) {
}
