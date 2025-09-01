package app.conferenceroom.service.model;

import java.util.List;

public record RoomModel(
        Long roomId,
        String name,
        int capacity,
        List<TimeRange> maintenanceTimings) {
}
