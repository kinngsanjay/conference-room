package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class BookingDto {
    private Long roomId;
    @Valid
    @NotNull(message = "timeRange must not be null")
    private MeetingTimeRange timeRange;
    @NotNull
    private int numberOfPeople;
}
