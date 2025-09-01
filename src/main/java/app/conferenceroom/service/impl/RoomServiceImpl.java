package app.conferenceroom.service.impl;


import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.db.mapper.RoomToModelMapper;
import app.conferenceroom.service.RoomService;
import app.conferenceroom.service.exception.RoomNotExistException;
import app.conferenceroom.service.model.CreateBookingCommand;
import app.conferenceroom.service.model.RoomModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private RoomAvailabilityService roomAvailabilityService;
    private RoomRepository roomRepository;
    private MaintenanceService maintenanceService;

    @Override
    public RoomModel getRoomByName(String name) {
        log.info("Get Room by name: {}", name);
        Room room = roomRepository.findByName(name)
                .orElseThrow(() -> new RoomNotExistException("Room with name %s does not exist".formatted(name)));
        RoomModel roomModel = RoomToModelMapper.toRoomModel(room, maintenanceService.getTimings());
        log.info("Room details: {}", roomModel);
        return roomModel;
    }

    @Override
    public List<RoomModel> getAvailableRooms(CreateBookingCommand bookingCommand, int limit) {
        log.info("getAvailableRooms, bookingCommand: {}, limit {}", bookingCommand, limit);
        var availableRooms = roomAvailabilityService.getAllAvailableRooms(bookingCommand);
        return availableRooms.stream().limit(limit).toList();
    }
}
