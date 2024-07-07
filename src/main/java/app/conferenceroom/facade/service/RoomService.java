package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.RoomAvailabilityResDto;
import app.conferenceroom.facade.dto.RoomDto;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {

    RoomDto getRoomByName(String name);

    RoomAvailabilityResDto getAvailableRooms(BookingDto bookingDto, boolean bestRoom);

}
