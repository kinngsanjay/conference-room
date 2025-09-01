package app.conferenceroom.service.model;

public record CreateBookingCommand(
        String roomName,
        TimeRange meetingTime,
        int numberOfAttendees) {
}

