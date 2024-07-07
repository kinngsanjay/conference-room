package app.conferenceroom.facade.dto;

import app.conferenceroom.facade.validator.numberOfPeople.ValidNumberOfPeople;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
public record RoomAvailabilityDto (
    @Valid
    @NotNull(message = "timeRange must not be null")
    MeetingTimeRange timeRange,
    @ValidNumberOfPeople
    int numberOfPeople
) {}
