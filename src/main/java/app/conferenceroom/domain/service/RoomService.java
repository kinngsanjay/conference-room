package app.conferenceroom.domain.service;

import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {

    RoomModel getRoomByName(String name);

    List<RoomModel> getAvailableRooms(BookingModel bookingModel, boolean bestRoom);

}
