package app.conferenceroom.service.model;

public record MeetingModel(
    TimeRange timeRange,
    int numberOfAttendees
) {}
