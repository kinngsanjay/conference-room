package app.conferenceroom.api.mapper;


import app.conferenceroom.api.dto.RoomDTO;
import app.conferenceroom.api.dto.TimeRangeDTO;
import app.conferenceroom.service.model.RoomModel;

public class RoomMapper {
    public static RoomDTO toDto(RoomModel roomModel) {
        var timings = roomModel.maintenanceTimings().stream()
                .map(dto -> new TimeRangeDTO(dto.startTime(), dto.endTime()))
                .toList();

        return new RoomDTO(roomModel.name(), roomModel.capacity(), timings);
    }
}
