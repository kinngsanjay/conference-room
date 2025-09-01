package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.BookingRequestDTO;
import app.conferenceroom.api.dto.TimeRangeDTO;
import app.conferenceroom.service.RoomService;
import app.conferenceroom.service.exception.RoomNotExistException;
import app.conferenceroom.service.model.CreateBookingCommand;
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
    public void testGetRoomByName() {
        assertEquals(roomService.getRoomByName("Inspire").roomId(), 3L);
    }

    @Test
    public void testGetRoomByNameWhichDoNotExist() {
        RoomNotExistException exception = assertThrows(RoomNotExistException.class, () -> {
            roomService.getRoomByName("TestRoom");
        });
    }

    @Test
    public void testGetRoomByName1() {
        LocalTime time = LocalTime.of(14, 0, 0);
        BookingRequestDTO bookingRequestDto = new BookingRequestDTO("Strive", getMeetingTimeRange(time), 12);
        var bookingRoom = new CreateBookingCommand(
                bookingRequestDto.roomName(),
                new TimeRange(
                        bookingRequestDto.timeRangeDTO().startTime(),
                        bookingRequestDto.timeRangeDTO().endTime()),
                bookingRequestDto.numberOfAttendees());
        assertNotNull(roomService.getAvailableRooms(bookingRoom, 10));
    }

}
