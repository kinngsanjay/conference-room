package app.conferenceroom.domain.model;

import app.conferenceroom.api.dto.RoomDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingModel {
    private String bookingReference;
    private RoomModel roomModel;
    private RoomDetailsDto roomDetailsDto;
}
