package app.conferenceroom.domain.service;


import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.db.mapper.RoomToModelMapper;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
import app.conferenceroom.infra.exception.ConferenceRoomException;
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
    private RoomToModelMapper roomToModelMapper;

    @Override
    public RoomModel getRoomByName(String name) {
        log.info("Get Room by name: {}", name);
        Room room = roomRepository.findByName(name)
                .orElseThrow(() -> new ConferenceRoomException(ErrorCode.NO_SUCH_ROOM));
        RoomModel roomModel = roomToModelMapper.apply(room);
        roomModel.setMaintenanceTimings(maintenanceService.getTimings());
        log.info("Room details: {}", roomModel);
        return roomModel;
    }

    @Override
    public List<RoomModel> getAvailableRooms(BookingModel bookingModel, boolean bestRoom) {
        log.info("getAvailableRooms, bookingModel: {}, bestRoom {}", bookingModel, bestRoom);
        var availableRooms = roomAvailabilityService.getAllAvailableRooms(bookingModel);
        return bestRoom ? List.of(availableRooms.get(0)) : availableRooms;
    }
}
