package app.conferenceroom.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RoomDetailsDto(
    @Valid
    @NotNull(message = "timeRange must not be null")
    MeetingTimeRange timeRange,
    @Min(value = 1, message = "Minimum 1 person is required")
    @Max(value = 20, message = "Maximum 20 people are allowed")
    @JsonProperty("numberOfPeople")
    int numberOfPeople
) {}
