package app.conferenceroom.facade.service;


import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.RoomAvailabilityResDto;
import app.conferenceroom.facade.dto.RoomDto;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private RoomAvailabilityService roomAvailabilityService;
    private RoomRepository roomRepository;
    private MaintenanceService maintenanceService;

    @Override
    public RoomDto getRoomByName(String name) {
        log.info("Get Room by name: {}", name);
        Room room = roomRepository.findByName(name);
        if(null == room) {
            log.info("NO Room FOUND!!!");
            throw new ConferenceRoomException(ErrorCode.NO_SUCH_ROOM);
        }
        RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(room, roomDto);
        roomDto.setMaintenanceTimings(maintenanceService.getTimings());
        log.info("Room details: {}", roomDto);
        return roomDto;
    }

    @Override
    public RoomAvailabilityResDto getAvailableRooms(BookingDto bookingDto, boolean bestRoom) {
        log.info("getAvailableRooms, bookingDto: {}, bestRoom {}", bookingDto, bestRoom);
        return RoomAvailabilityResDto.builder()
                .availableRooms(roomAvailabilityService.getAvailableRooms(bookingDto, bestRoom)).build();
    }
}
