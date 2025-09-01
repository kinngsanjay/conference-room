package app.conferenceroom.service.model;

public record  BookingRecord(
        String bookingReference,
        String roomName,
        TimeRange meetingTime,
        int numberOfAttendees) {
}
