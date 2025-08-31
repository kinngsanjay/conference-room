package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.BookingRequestDto;
import app.conferenceroom.api.dto.MeetingRequestDto;
import app.conferenceroom.api.dto.TimeRange;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.MeetingModel;
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

    private TimeRange getMeetingTimeRange(LocalTime startTime) {
        return new TimeRange(startTime, startTime.plusMinutes(15));
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
        BookingRequestDto bookingRequestDto =  new BookingRequestDto(
                "Strive", new MeetingRequestDto(getMeetingTimeRange(time), 12));
        var bookingModel = new BookingModel();
        bookingModel.setRoomModel(roomService.getRoomByName(bookingRequestDto.roomName()));
        bookingModel.setMeetingModel(new MeetingModel(bookingRequestDto.meetingRequestDto().timeRange(),
                bookingRequestDto.meetingRequestDto().numberOfPeople()));
        assertNotNull(roomService.getAvailableRooms(bookingModel, false));
    }

}
