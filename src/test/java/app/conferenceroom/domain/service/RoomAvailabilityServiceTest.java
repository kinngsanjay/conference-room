package app.conferenceroom.domain.service;

import app.conferenceroom.api.dto.MeetingTimeRange;
import app.conferenceroom.api.dto.RoomDetailsDto;
import app.conferenceroom.domain.enums.ErrorCode;
import app.conferenceroom.domain.model.BookingModel;
import app.conferenceroom.domain.model.RoomModel;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomAvailabilityServiceTest {
    @Autowired
    RoomAvailabilityService roomAvailabilityService;

    private MeetingTimeRange getMeetingTimeRange() {
        LocalTime time = LocalTime.of(14, 0, 0);
        return new MeetingTimeRange(time, time.plusHours(1));
    }

    @Test
    public void testAvailabilityWithoutRoom() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        BookingModel bookingModel = new BookingModel(null, new RoomModel(), new RoomDetailsDto(
                new MeetingTimeRange(startTime, startTime.plusHours(1)), 5));

        List<RoomModel> rooms = roomAvailabilityService.getAllAvailableRooms(bookingModel);
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
        assertEquals(2, rooms.size());
        assertEquals("Beauty", rooms.get(0).getName());
    }

    @Test
    public void testAvailabilityWithEndTimeBeforeBookingStartTime() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        BookingModel bookingModel = new BookingModel(null, new RoomModel(), new RoomDetailsDto(
                new MeetingTimeRange(startTime, startTime.minusHours(1)), 5));

        List<RoomModel> rooms = roomAvailabilityService.getAllAvailableRooms(bookingModel);

        assertEquals("Beauty", rooms.get(0).getName());
    }

    @Test
    public void testGetAllBookings() {
        LocalTime time = LocalTime.of(10, 0, 0);
        MeetingTimeRange timeRange = new MeetingTimeRange(time.minusHours(1), time.plusHours(1));

        List<BookingModel> bookings = roomAvailabilityService.getAllBookings(timeRange);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getRoomModel().getRoomId());
    }

    @Test()
    public void testGetAllBookingsNoBookingsFound() {
        LocalTime time = LocalTime.of(13, 0, 0);
        MeetingTimeRange timeRange = new MeetingTimeRange(time.plusHours(2), time.plusHours(4));

        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            roomAvailabilityService.getAllBookings(timeRange);
        });

        assertEquals(ErrorCode.NO_BOOKING_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test()
    public void testGetAllBookingsNoBookingsFoundDueToCapacity() {
        BookingModel bookingModel = new BookingModel(null, new RoomModel(3L, "Inspire"),
                new RoomDetailsDto(getMeetingTimeRange(), 22));

        assertEquals( roomAvailabilityService.getAllAvailableRooms(bookingModel).size(), 0);
    }

    @Test()
    public void testWhenRoomIsUnderMaintenance() {
        LocalTime time = LocalTime.of(9, 0, 0);
        BookingModel bookingDto = new BookingModel(null,
                new RoomModel(3L, "Inspire"),
                new RoomDetailsDto(new MeetingTimeRange(time, time.plusMinutes(15)), 22));

        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            roomAvailabilityService.getAllAvailableRooms(bookingDto);
        });

        assertEquals(ErrorCode.ROOM_UNDER_MAINTENANCE.getErrorCode(), exception.getErrorCode());
    }
}
