package app.conferenceroom.facade.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RoomAvailabilityResDto {
    private List<String> availableRooms;
}
