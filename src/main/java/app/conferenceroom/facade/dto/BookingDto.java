package app.conferenceroom.facade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingDto {
        Long roomId;
        @NotNull(message = "meetingDetails must not be null")
        @Valid
        RoomAvailabilityDto meetingDetails;
}
