package app.conferenceroom.service;

import app.conferenceroom.service.model.RoomModel;
import app.conferenceroom.service.model.SearchRoomsCommand;
import app.conferenceroom.service.model.SearchRoomsResult;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {

    RoomModel searchRoomByName(String name);

    SearchRoomsResult execute(SearchRoomsCommand command, int limit);

}
