package app.conferenceroom.service.impl;


import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.db.mapper.RoomToModelMapper;
import app.conferenceroom.service.RoomService;
import app.conferenceroom.service.exception.RoomNotExistException;
import app.conferenceroom.service.model.RoomModel;
import app.conferenceroom.service.model.SearchRoomsCommand;
import app.conferenceroom.service.model.SearchRoomsResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private RoomAvailabilityService roomAvailabilityService;
    private RoomRepository roomRepository;
    private MaintenanceService maintenanceService;

    @Override
    public RoomModel searchRoomByName(String name) {
        log.info("Get Room by name: {}", name);
        Room room = roomRepository.findByName(name)
                .orElseThrow(() -> new RoomNotExistException("Room with name %s does not exist".formatted(name)));
        RoomModel roomModel = RoomToModelMapper.toRoomModel(room, maintenanceService.getTimings());
        log.info("Room details: {}", roomModel);
        return roomModel;
    }

    @Override
    public SearchRoomsResult execute(SearchRoomsCommand command, int limit) {
        log.info("getAvailableRooms, bookingCommand: {}, limit {}", command, limit);
        var availableRooms = roomAvailabilityService.searchAvailableRooms(
                command.meetingTime(), command.numberOfAttendees());
        return new SearchRoomsResult(availableRooms.stream().limit(limit).toList());
    }
}
