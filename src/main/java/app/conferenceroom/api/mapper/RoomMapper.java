package app.conferenceroom.api.mapper;


import app.conferenceroom.api.dto.RoomDTO;
import app.conferenceroom.api.dto.SearchRoomRequestDTO;
import app.conferenceroom.api.dto.TimeRangeDTO;
import app.conferenceroom.service.model.RoomModel;
import app.conferenceroom.service.model.SearchRoomsCommand;
import app.conferenceroom.service.model.TimeRange;
import jakarta.validation.Valid;

public class RoomMapper {
    public static RoomDTO toDto(RoomModel roomModel) {
        var timings = roomModel.maintenanceTimings().stream()
                .map(dto -> new TimeRangeDTO(dto.startTime(), dto.endTime()))
                .toList();

        return new RoomDTO(roomModel.name(), roomModel.capacity(), timings);
    }

    public static SearchRoomsCommand toSearchRoomCommad(@Valid SearchRoomRequestDTO searchRoomRequestDTO) {
        var timeRangeDTO = searchRoomRequestDTO.timeRangeDTO();
        var timeRange = new TimeRange(timeRangeDTO.startTime(), timeRangeDTO.endTime());

        return new SearchRoomsCommand(timeRange, searchRoomRequestDTO.numberOfAttendees());
    }
}
