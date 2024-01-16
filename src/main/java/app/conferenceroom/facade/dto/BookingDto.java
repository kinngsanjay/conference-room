package app.conferenceroom.facade.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class BookingDto {
    private Long roomId;
    private MeetingTimeRange timeRange;
    @NotNull
    private int numberOfPeople;
}
