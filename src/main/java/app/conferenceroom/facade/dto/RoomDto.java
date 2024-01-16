package app.conferenceroom.facade.dto;

import lombok.Data;

import java.util.List;
@Data
public class RoomDto {
    private Long roomId;
    private String name;
    private int capacity;
    private List<MaintenanceTimeRange> maintenanceTimings;
}
