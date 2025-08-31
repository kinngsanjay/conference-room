package app.conferenceroom.api.dto;

import app.conferenceroom.domain.validator.meetingrange.ValidTimeRange;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MeetingRequestDto(
    @Valid
    @NotNull(message = "timeRange must not be null")
    @ValidTimeRange
    TimeRange timeRange,
    @Min(value = 1, message = "Minimum 1 person is required")
    @Max(value = 20, message = "Maximum 20 people are allowed")
    @JsonProperty("numberOfPeople")
    int numberOfPeople
) {}
