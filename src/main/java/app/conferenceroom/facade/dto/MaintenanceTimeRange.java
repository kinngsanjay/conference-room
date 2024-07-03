package app.conferenceroom.facade.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record MaintenanceTimeRange(
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        LocalTime endTime
) {
}
