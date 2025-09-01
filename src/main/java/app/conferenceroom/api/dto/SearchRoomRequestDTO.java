package app.conferenceroom.api.dto;

import app.conferenceroom.validator.meetingrange.ValidTimeRange;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SearchRoomRequestDTO(
        @JsonProperty("timeRange")
        @ValidTimeRange
        TimeRangeDTO timeRangeDTO,
        @JsonProperty("numberOfAttendees")
        @Min(value = 1, message = "Number of attendees should be more than 0")
        @Max(value = 50, message = "Number of attendees should be not more than 50")
        int numberOfAttendees
) {
}
