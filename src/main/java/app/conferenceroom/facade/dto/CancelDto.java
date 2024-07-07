package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CancelDto(
        @NotNull(message = "roomId must not be null")
        Long roomId,
        @Valid
        @NotNull(message = "timeRange must not be null")
        MeetingTimeRange timeRange) {
}
