package app.conferenceroom.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingModel {
    private String bookingReference;
    private RoomModel roomModel;
    private MeetingModel meetingModel;
}
