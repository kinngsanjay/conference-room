package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;


public record TimeRangeDTO(
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        @JsonProperty("startTime")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
        @JsonProperty("endTime")
        LocalTime endTime) {
}
