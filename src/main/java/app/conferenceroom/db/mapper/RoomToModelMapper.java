package app.conferenceroom.db.mapper;

import app.conferenceroom.db.entity.Room;
import app.conferenceroom.service.model.RoomModel;
import app.conferenceroom.service.model.TimeRange;

import java.util.List;

public class RoomToModelMapper {
    public static RoomModel toRoomModel(Room room, List<TimeRange> maintenanceTimings) {
        return new RoomModel(room.getRoomId(), room.getName(), room.getCapacity(), maintenanceTimings);
    }
}
