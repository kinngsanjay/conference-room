package app.conferenceroom.api.dto;

public record BookingResponseDto (
        String bookingReference,
        String roomName,
        MeetingResponseDto meetingResponseDto
)
{ }
