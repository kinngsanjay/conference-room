package app.conferenceroom.facade.dto;

import app.conferenceroom.facade.validator.meetingrange.ValidMeetingTimeRange;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@ValidMeetingTimeRange
public record MeetingTimeRange(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
        LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
        LocalDateTime endTime) {
}
