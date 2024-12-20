package app.conferenceroom.facade.dto;

import app.conferenceroom.facade.validator.meetingrange.ValidMeetingTimeRange;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

@ValidMeetingTimeRange
public record MeetingTimeRange(
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        LocalTime endTime) {
}
