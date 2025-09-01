package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.SearchRoomRequestDTO;
import app.conferenceroom.api.dto.TimeRangeDTO;
import app.conferenceroom.service.RoomService;
import app.conferenceroom.service.exception.RoomNotExistException;
import app.conferenceroom.service.model.SearchRoomsCommand;
import app.conferenceroom.service.model.TimeRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomServiceTest {
    @Autowired
    RoomService roomService;

    private TimeRangeDTO getMeetingTimeRange(LocalTime startTime) {
        return new TimeRangeDTO(startTime, startTime.plusMinutes(15));
    }

    @Test
    public void testSearchRoomByName() {
        assertEquals(roomService.searchRoomByName("Inspire").roomId(), 3L);
    }

    @Test
    public void testSearchRoomByNameWhichDoNotExist() {
        RoomNotExistException exception = assertThrows(RoomNotExistException.class, () -> {
            roomService.searchRoomByName("TestRoom");
        });
    }

    @Test
    public void testSearchRoomByTimeAndAttendee() {
        LocalTime time = LocalTime.of(14, 0, 0);
        SearchRoomRequestDTO searchRoomRequestDTO = new SearchRoomRequestDTO(
                getMeetingTimeRange(time), 12);
        var bookingRoom = new SearchRoomsCommand(
                new TimeRange(
                        searchRoomRequestDTO.timeRangeDTO().startTime(),
                        searchRoomRequestDTO.timeRangeDTO().endTime()),
                searchRoomRequestDTO.numberOfAttendees());
        assertNotNull(roomService.execute(bookingRoom, 10));
    }

}
