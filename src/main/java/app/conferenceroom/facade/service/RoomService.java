package app.conferenceroom.facade.service;

import app.conferenceroom.db.entity.Room;
import app.conferenceroom.db.repository.RoomRepository;
import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.dto.RoomAvailabilityResDto;
import app.conferenceroom.facade.dto.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {

    private RoomAvailabilityService roomAvailabilityService;
    private RoomRepository roomRepository;
    private MaintenanceService maintenanceService;

    public RoomDto getRoomByName(String name) {
        Room room = roomRepository.findByName(name);
        RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(room, roomDto);
        roomDto.setMaintenanceTimings(maintenanceService.getTimings());
        return roomDto;
    }

    public RoomAvailabilityResDto getAvailableRooms(RoomAvailabilityDto availabilityDto) {
        return RoomAvailabilityResDto.builder()
                .availableRooms(roomAvailabilityService.getAvailableRooms(availabilityDto)).build();
    }

    public boolean isRoomAvailable(BookingDto booking) {
        return roomAvailabilityService.isRoomAvailableForBooking(booking);
    }
}
