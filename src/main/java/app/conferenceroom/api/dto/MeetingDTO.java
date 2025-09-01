package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MeetingDTO(
        @JsonProperty("roomName")
        String roomName,
        @JsonProperty("timeRange")
        TimeRangeDTO timeRangeDTO,
        @JsonProperty("numberOfAttendees")
        int numberOfAttendees
) {
}
