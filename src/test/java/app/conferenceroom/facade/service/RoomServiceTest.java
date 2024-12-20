package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomServiceTest {
    @Autowired
    RoomService roomService;

    private MeetingTimeRange getMeetingTimeRange(LocalTime startTime) {
        return new MeetingTimeRange(startTime, startTime.plusMinutes(15));
    }

    @Test
    public void testGetRoomByName() {
        assertEquals(roomService.getRoomByName("Inspire").getRoomId(), 3L);
    }

    @Test
    public void testGetRoomByNameWhichDoNotExist() {
        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            roomService.getRoomByName("TestRoom");
        });

        assertEquals(ErrorCode.NO_SUCH_ROOM.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void testGetRoomByName1() {
        LocalTime time = LocalTime.of(14, 0, 0);
        BookingDto bookingDto =  new BookingDto(
                4L, new RoomAvailabilityDto(getMeetingTimeRange(time), 12));
        assertNotNull(roomService.getAvailableRooms(bookingDto, false).getAvailableRooms());
    }

}
