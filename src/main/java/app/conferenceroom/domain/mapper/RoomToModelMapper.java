package app.conferenceroom.domain.mapper;

import app.conferenceroom.db.entity.Room;
import app.conferenceroom.domain.model.RoomModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RoomToModelMapper implements Function<Room, RoomModel> {
    public RoomModel apply(Room room) {
        var roomModel = new RoomModel();
        roomModel.setRoomId(room.getRoomId());
        roomModel.setName(room.getName());
        roomModel.setCapacity(room.getCapacity());
        return roomModel;
    }
}
