package app.conferenceroom.api.dto;

import app.conferenceroom.domain.validator.meetingrange.ValidTimeRange;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;


public record TimeRange(
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        LocalTime endTime) {
}
