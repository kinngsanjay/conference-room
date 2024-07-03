package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record RoomAvailabilityDto (
    @Valid
    @NotNull(message = "timeRange must not be null")
    MeetingTimeRange timeRange,
    @Min(value = 1, message = "At least 1 person should be in the meeting")
    @Max(value = 20, message = "Max 20 person can be allowed in a meeting")
    int numberOfPeople
) {}
