package app.conferenceroom.api.dto;

import app.conferenceroom.domain.validator.meetingrange.ValidTimeRange;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MeetingResponseDto(
        @JsonProperty("timeRange")
        TimeRange timeRange,
        @JsonProperty("numberOfPeople")
        int numberOfPeople
) {
}
