package app.conferenceroom.service;

import app.conferenceroom.service.model.CreateBookingCommand;
import app.conferenceroom.service.model.RoomModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {

    RoomModel getRoomByName(String name);

    List<RoomModel> getAvailableRooms(CreateBookingCommand bookingCommand, int limit);

}
