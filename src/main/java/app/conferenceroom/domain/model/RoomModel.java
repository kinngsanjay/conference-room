package app.conferenceroom.domain.model;

import app.conferenceroom.api.dto.MaintenanceTimeRange;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomModel {
    private Long roomId;
    private String name;
    private int capacity;
    private List<MaintenanceTimeRange> maintenanceTimings;

    public RoomModel(Long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}
