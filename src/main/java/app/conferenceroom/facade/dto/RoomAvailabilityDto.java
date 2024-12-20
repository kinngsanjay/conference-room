package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record RoomAvailabilityDto (
    @Valid
    @NotNull(message = "timeRange must not be null")
    MeetingTimeRange timeRange,
    @Min(value = 1, message = "Minimum 1 person is required")
    @Max(value = 100, message = "Maximum 100 people are allowed")
    int numberOfPeople
) {}
