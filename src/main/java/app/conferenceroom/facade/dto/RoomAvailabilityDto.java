package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomAvailabilityDto {
    @Valid
    @NotNull(message = "timeRange must not be null")
    private MeetingTimeRange timeRange;
    @Min(value = 1, message = "At least 1 person should be in the meeting")
    @Max(value = 20, message = "Max 20 person can be allowed in a meeting")
    private int numberOfPeople;
}
