package app.conferenceroom.facade.service;

import app.conferenceroom.facade.dto.BookingDto;
import app.conferenceroom.facade.dto.MeetingTimeRange;
import app.conferenceroom.facade.dto.RoomAvailabilityDto;
import app.conferenceroom.facade.enums.ErrorCode;
import app.conferenceroom.infra.exception.ConferenceRoomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class RoomAvailabilityServiceTest {
    @Autowired
    RoomAvailabilityService roomAvailabilityService;

    private MeetingTimeRange getMeetingTimeRange() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(14, 0, 0));
        return new MeetingTimeRange(time, time.plusHours(1));
    }

    @Test
    public void testGetAvailableRoomsBestRoom() {
        BookingDto bookingDto = new BookingDto(
                4L, new RoomAvailabilityDto(getMeetingTimeRange(), 12));
        List<String> availableRooms = roomAvailabilityService.getAvailableRooms(bookingDto, false);

        assertNotNull(availableRooms);
        assertFalse(availableRooms.isEmpty());
        assertEquals(2, availableRooms.size());

        availableRooms = roomAvailabilityService.getAvailableRooms(bookingDto, true);

        assertNotNull(availableRooms);
        assertFalse(availableRooms.isEmpty());
        assertEquals(1, availableRooms.size());
        assertEquals("Inspire", availableRooms.get(0));
    }

    @Test
    public void testConfirmBookingWithoutRoomId() {
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.of(10, 0, 0));
        BookingDto bookingDto = new BookingDto(null, new RoomAvailabilityDto(
                new MeetingTimeRange(startTime, startTime.plusHours(1)), 5));

        String roomName = roomAvailabilityService.confirmBooking(bookingDto);

        assertEquals("Beauty", roomName);
    }

    @Test
    public void testConfirmBookingWithEndTimeBeforeBookingStartTime() {
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.of(10, 0, 0));
        BookingDto bookingDto = new BookingDto(null, new RoomAvailabilityDto(
                new MeetingTimeRange(startTime, startTime.minusHours(1)), 5));

        String roomName = roomAvailabilityService.confirmBooking(bookingDto);

        assertEquals("Beauty", roomName);
    }

    @Test
    public void testConfirmBookingWithRoomId() {
        BookingDto bookingDto = new BookingDto(
                3L, new RoomAvailabilityDto(getMeetingTimeRange(), 5));

        String roomName = roomAvailabilityService.confirmBooking(bookingDto);

        assertEquals("Inspire", roomName);
    }

    @Test
    public void testGetAllBookings() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(10, 0, 0));
        MeetingTimeRange timeRange = new MeetingTimeRange(time.minusHours(1), time.plusHours(1));

        List<BookingDto> bookings = roomAvailabilityService.getAllBookings(timeRange);

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getRoomId());
    }

    @Test()
    public void testGetAllBookingsNoBookingsFound() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(13, 0, 0));
        MeetingTimeRange timeRange = new MeetingTimeRange(time.plusHours(2), time.plusHours(4));

        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            roomAvailabilityService.getAllBookings(timeRange);
        });

        assertEquals(ErrorCode.NO_BOOKING_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test()
    public void testGetAllBookingsNoBookingsFoundDueToCapacity() {
        BookingDto bookingDto = new BookingDto(
                3L, new RoomAvailabilityDto(getMeetingTimeRange(), 22));

        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            roomAvailabilityService.confirmBooking(bookingDto);
        });

        assertEquals(ErrorCode.NO_SUCH_ROOM.getErrorCode(), exception.getErrorCode());
    }

    @Test()
    public void testWhenRoomIsUnderMaintenance() {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.of(9, 0, 0));
        BookingDto bookingDto = new BookingDto(
                3L, new RoomAvailabilityDto(new MeetingTimeRange(time, time.plusMinutes(15)), 22));

        ConferenceRoomException exception = assertThrows(ConferenceRoomException.class, () -> {
            roomAvailabilityService.confirmBooking(bookingDto);
        });

        assertEquals(ErrorCode.ROOM_UNDER_MAINTENANCE.getErrorCode(), exception.getErrorCode());
    }
}
