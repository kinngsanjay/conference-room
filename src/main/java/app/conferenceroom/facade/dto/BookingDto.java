package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record BookingDto(
        @NotNull(message = "roomId must not be null")
        Long roomId,
        @Valid
        @NotNull(message = "timeRange must not be null")
        MeetingTimeRange timeRange,
        @Min(value = 1, message = "Minimum numberOfPeople required")
        @Max(value = 20, message = "Maximum 20 people allowed")
        int numberOfPeople) {
}
